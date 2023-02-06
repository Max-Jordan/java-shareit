package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.ShortBooking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.CommentResponseDto;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private static final String NOT_FOUND = " not found";

    @Override
    public ItemResponseDto saveItem(Long ownerId, ItemDto dto) {
        Item item = ItemMapper.mapToItem(dto);
        item.setIdOwner(userService.getUserById(ownerId).getId());
        log.info("A request was received to save item");
        return ItemMapper.mapToItemResponseDto(itemRepository.save(item));
    }

    @Override
    public List<ItemResponseDto> getItemsByUser(Long ownerId) {
        log.info("A request was received to receive the user's items");
        return itemRepository.findAll().stream()
                .filter(x -> Objects.equals(x.getIdOwner(), ownerId))
                .peek(item -> {
                    List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStartDesc(item.getId());
                    item.setNextBooking(getNextBooking(bookings));
                    item.setLastBooking(getPreviousBooking(bookings));
                    item.setComments(commentRepository.findAllByItemId(item.getId()).stream()
                            .map(CommentMapper::mapToCommentResponseDto)
                            .collect(Collectors.toList()));
                })
                .map(ItemMapper::mapToItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemById(Long itemId, Long userId) {
        log.info("A request was received to receive item by id");
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
        if (Objects.equals(item.getIdOwner(), userId)) {
            List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStartDesc(itemId);
            item.setNextBooking(getNextBooking(bookings));
            item.setLastBooking(getPreviousBooking(bookings));
        }
        item.setComments(commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::mapToCommentResponseDto)
                .collect(Collectors.toList()));
        return ItemMapper.mapToItemResponseDto(item);
    }

    @Override
    public ItemResponseDto editItem(Long ownerId, Long itemId, ItemDto item) {
        log.info("A request was received to edit item with id " + itemId);
        Item updateItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + NOT_FOUND));
        if (!Objects.equals(updateItem.getIdOwner(), ownerId)) {
            throw new NotFoundException("This item does not belong to this user");
        }
        Optional.ofNullable(item.getDescription()).ifPresent(updateItem::setDescription);
        Optional.ofNullable(item.getName()).ifPresent(updateItem::setName);
        Optional.ofNullable(item.getAvailable()).ifPresent(updateItem::setAvailable);
        return ItemMapper.mapToItemResponseDto(itemRepository.saveAndFlush(updateItem));
    }

    @Override
    public List<ItemResponseDto> getItemBySearch(String name) {
        log.info("A request was received to receive item by name or description");
        if (StringUtils.isEmpty(name)) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByNameOrDescriptionContainingIgnoreCase(name,name).stream()
                .filter(item -> BooleanUtils.isTrue(item.getAvailable()))
                .map(ItemMapper::mapToItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto addComment(Long itemId, Long userId, CommentDto dto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
        User user = UserMapper.mapToUser(userService.getUserById(userId));

        List<Booking> bookings = bookingRepository.findByItemIdAndBookerIdAndEndIsBeforeOrderByStartDesc(
                itemId, userId, LocalDateTime.now());

        if (bookings.isEmpty()) {
            throw new BookingException("This user not book this item");
        }
        Comment comment = CommentMapper.mapToComment(dto);
        comment.setItemId(item);
        comment.setAuthorName(user);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.mapToCommentResponseDto(commentRepository.save(comment));
    }

    private ShortBooking getNextBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .map(BookingMapper::mapToShortBooking)
                .findFirst()
                .orElseGet(() -> null);
    }

    private ShortBooking getPreviousBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .map(BookingMapper::mapToShortBooking)
                .findFirst()
                .orElseGet(() -> null);
    }
}
