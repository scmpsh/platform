package com.override.services;

import com.override.PlatformBot;
import dtos.ResponseJoinRequestDTO;
import dtos.StudentAccountDTO;
import enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinRequestService {

    private final PlatformBot platformBot;

    public void processJoinRequestResponse(ResponseJoinRequestDTO responseDTO) {
        if (responseDTO.getStatus() == RequestStatus.APPROVED) {
            approvedResponse(responseDTO.getAccountDTO());
        }
        if (responseDTO.getStatus() == RequestStatus.DECLINED) {
            declinedResponse(responseDTO.getAccountDTO());
        }
    }

    public void sendMessage(String chatId, String text) {
        platformBot.sendMessage(chatId, text);
    }

    private void approvedResponse(StudentAccountDTO student) {
        sendMessage(student.getTelegramChatId(), "login: " + student.getLogin() + "\n" + "password: " + student.getPassword());
    }

    private void declinedResponse(StudentAccountDTO student) {
        sendMessage(student.getTelegramChatId(), "Ваш запрос не был одобрен, повторите попытку");
    }
}

