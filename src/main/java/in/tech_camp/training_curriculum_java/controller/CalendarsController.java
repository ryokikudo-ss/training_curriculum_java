package in.tech_camp.training_curriculum_java.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.training_curriculum_java.repository.PlanRepository;
import in.tech_camp.training_curriculum_java.form.PlanForm;
import in.tech_camp.training_curriculum_java.entity.PlanEntity;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CalendarsController {

  private final PlanRepository planRepository;

  // 1週間のカレンダーと予定が表示されるページ
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("planForm", new PlanForm());
    List<Map<String, Object>> weekPlans = getWeek();
    model.addAttribute("weekPlans", weekPlans);
    return "calendars/index";
  }
ssue2
  // 予定の保存
  @PostMapping("/calendars")
  public String create(@ModelAttribute("planForm") @Validated PlanForm planForm, BindingResult result) {
    if (!result.hasErrors()) {
      PlanEntity newPlan = new PlanEntity();
      newPlan.setDate(planForm.getDate());
      newPlan.setPlan(planForm.getPlan());
      planRepository.insert(newPlan);
    }
    return "redirect:/calendars";
  }

  private List<Map<String, Object>> getWeek() {
    List<Map<String, Object>> weekPlans = new ArrayList<>();

    LocalDate today = LocalDate.now();
    List<PlanEntity> plans = planRepository.findByDateBetween(today, today.plusDays(6));

    String[] weekDayLabels = {"(日)", "(月)", "(火)", "(水)", "(木)", "(金)", "(土)"};

    for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
      Map<String, Object> dayData = new HashMap<>();
      LocalDate currentDate = today.plusDays(dayOffset);

      List<String> plansForDate = new ArrayList<>();
      for (PlanEntity plan : plans) {
          if (plan.getDate().equals(currentDate)) {
              plansForDate.add(plan.getPlan());
          }
      }

      dayData.put("month", currentDate.getMonthValue());
      dayData.put("date", currentDate.getDayOfMonth());
      dayData.put("plans", plansForDate);

      weekPlans.add(dayData);
    }

    return weekPlans;
  }


}
