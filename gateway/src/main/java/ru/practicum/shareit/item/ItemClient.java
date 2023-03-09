package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/items"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> saveItem(long userId, ItemDto dto) {
        return post("/", userId, dto);
    }

    public ResponseEntity<Object> getItemById(Long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsByUser(Long index, Long size, long userId) {
        Map<String, Object> parameters = Map.of("index", index, "size", size);
        return get("?index={index}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getItemBySearch(String text, Long userId, Integer index, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "index", index,
                "size", size
        );
        return get("/search?text={text}&index=index%size={size}", userId, parameters);
    }

    public ResponseEntity<Object> editItem(ItemDto dto, long itemId, long userId) {
        return patch("/" + itemId, userId, dto);
    }

    public ResponseEntity<Object> postComment(long itemId, long userId, CommentDto dto) {
        return post("/" + itemId + "/comment", userId, dto);
    }
}
