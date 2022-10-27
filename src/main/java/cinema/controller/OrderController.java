package cinema.controller;

import cinema.dto.response.OrderResponseDto;
import cinema.model.Order;
import cinema.model.ShoppingCart;
import cinema.model.User;
import cinema.service.OrderService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;
import cinema.service.mapper.ResponseDtoMapper;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    private final UserService userService;
    private final ResponseDtoMapper<OrderResponseDto, Order> orderResponseDtoMapper;

    public OrderController(ShoppingCartService shoppingCartService,
                           OrderService orderService,
                           UserService userService,
                           ResponseDtoMapper<OrderResponseDto, Order> orderResponseDtoMapper) {
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
        this.userService = userService;
        this.orderResponseDtoMapper = orderResponseDtoMapper;
    }

    @PostMapping("/complete")
    public OrderResponseDto completeOrder(Authentication auth) {
        ShoppingCart cart = shoppingCartService.getByUser(getAuthenticatedUser(auth));
        return orderResponseDtoMapper.mapToDto(orderService.completeOrder(cart));
    }

    @GetMapping
    public List<OrderResponseDto> getOrderHistory(Authentication auth) {
        return orderService.getOrdersHistory(getAuthenticatedUser(auth))
                .stream()
                .map(orderResponseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    private User getAuthenticatedUser(Authentication auth) {
        String email = auth.getName();
        return userService.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("User with email " + email + " not found"));
    }
}
