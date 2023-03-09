package ru.practicum.shareit.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.springframework.data.domain.PageRequest.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationMapper {

    public static Pageable mapToPageable(Integer from, Integer size) {
        return PageRequest.of(from / size, size);
    }

    public static Pageable mapToPageableWithSort(Integer from, Integer size, Sort sort) {
        return PageRequest.of(from / size, size, sort);
    }
}
