package com.example.QuickFixersBackend.services.serviceInterfce;

import java.io.IOException;

public interface ChatbotService {
    String reply(String message) throws IOException;
}
