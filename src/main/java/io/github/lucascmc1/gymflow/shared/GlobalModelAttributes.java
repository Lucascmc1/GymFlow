package io.github.lucascmc1.gymflow.shared;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private static final DateTimeFormatter FULL_DATE = DateTimeFormatter.ofPattern(
            "EEEE, dd 'de' MMMM", Locale.forLanguageTag("pt-BR"));

    @ModelAttribute("todayLabel")
    String todayLabel() {
        String formatted = LocalDate.now().format(FULL_DATE);
        return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
    }

    @ModelAttribute("currentUsername")
    String currentUsername(Principal principal) {
        return principal == null ? "" : principal.getName();
    }
}
