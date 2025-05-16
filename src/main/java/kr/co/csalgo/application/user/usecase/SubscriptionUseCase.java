package kr.co.csalgo.application.user.usecase;

import kr.co.csalgo.application.user.dto.SubscriptionUseCaseDto;
import kr.co.csalgo.domain.user.entity.User;
import kr.co.csalgo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionUseCase {

    private final UserService userService;

    public SubscriptionUseCaseDto.Response create(SubscriptionUseCaseDto.Request request) {
        User user = userService.create(request.getEmail());
        return SubscriptionUseCaseDto.Response.fromEntity(user);
    }

}
