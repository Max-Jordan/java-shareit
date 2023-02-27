package ru.practicum.shareit.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationMapper {

    public static Pageable mapToPageable(Integer from, Integer size) {
        return PageRequest.of(from/size, size);
    }
}
