package io.github.lucascmc1.gymflow.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"/", "/dashboard"})
    String dashboard(Model model) {
        model.addAttribute("dashboard", dashboardService.load());
        return "dashboard";
    }
}
