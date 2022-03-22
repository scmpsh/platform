package com.override.service;

import com.override.feigns.TelegramBotFeign;
import com.override.mappers.JoinRequestMapper;
import com.override.mappers.PlatformUserMapper;
import com.override.models.JoinRequest;
import com.override.models.PlatformUser;
import com.override.repositories.JoinRequestRepository;
import dtos.JoinRequestStatusDTO;
import dtos.RegisterUserRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class JoinRequestServiceTest {

    @InjectMocks
    private JoinRequestService joinRequestService;

    @Mock
    private JoinRequestRepository requestRepository;

    @Mock
    private TelegramBotFeign telegramBotFeign;

    @Mock
    private JoinRequestMapper joinRequestMapper;

    @Mock
    private PlatformUserService accountService;

    @Mock
    private PlatformUserMapper accountMapper;


    @Test
    public void testWhenAlreadyExistJoinRequest() {
        RegisterUserRequestDTO requestDTO = RegisterUserRequestDTO.builder()
                .telegramUserName("Marandyuk_Anatolii")

                .chatId("1234")
                .build();
        when(requestRepository.findFirstByChatId("1234")).thenReturn(new JoinRequest(1L, "Marandyuk_Anatolii", "1234"));

        JoinRequestStatusDTO joinRequestStatusDTO = joinRequestService.saveRequest(requestDTO);

        Assertions.assertEquals(new JoinRequestStatusDTO("В этом чате уже есть запрос на регистрацию"), joinRequestStatusDTO);
    }


    @Test
    public void testWhenAlreadyExistUserInPlatform() {
        RegisterUserRequestDTO requestDTO = RegisterUserRequestDTO.builder()
                .telegramUserName("Marandyuk_Anatolii")

                .chatId("1234")
                .build();

        when(requestRepository.findFirstByChatId("1234")).thenReturn(null);
        when(accountService.getAccountByChatId("1234")).thenReturn(new PlatformUser());


        JoinRequestStatusDTO joinRequestStatusDTO = joinRequestService.saveRequest(requestDTO);

        Assertions.assertEquals(new JoinRequestStatusDTO("Вы уже зарегистрированы"), joinRequestStatusDTO);
    }

    @Test
    public void testWhenNewUser() {
        RegisterUserRequestDTO requestDTO = RegisterUserRequestDTO.builder()
                .telegramUserName("Marandyuk_Anatolii")
                .chatId("1234")
                .build();
        JoinRequest requestEntity = new JoinRequest(1L, "Marandyuk_Anatolii", "1234");

        when(requestRepository.findFirstByChatId("1234")).thenReturn(null);
        when(accountService.getAccountByChatId("1234")).thenReturn(null);
        when(joinRequestMapper.dtoToEntity(any())).thenReturn(requestEntity);

        JoinRequestStatusDTO joinRequestStatusDTO = joinRequestService.saveRequest(requestDTO);

        Assertions.assertEquals(new JoinRequestStatusDTO("Ваш запрос на регистрацию в платформе создан, ожидайте подтверждения"),
                joinRequestStatusDTO);
        verify(requestRepository, times(1)).save(requestEntity);
    }
}