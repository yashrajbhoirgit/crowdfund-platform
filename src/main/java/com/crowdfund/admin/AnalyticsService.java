package com.crowdfund.admin;

import com.crowdfund.admin.dto.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AnalyticsService {
    // In a real scenario, this would use Repositories to fetch data.
    // Here we provide dummy data that works for the UI to represent the backend.

    public List<MonthlyDataDto> getMonthlyDonationData() {
        List<MonthlyDataDto> data = new ArrayList<>();
        data.add(new MonthlyDataDto("Jan", new BigDecimal("12000"), 150));
        data.add(new MonthlyDataDto("Feb", new BigDecimal("15000"), 180));
        data.add(new MonthlyDataDto("Mar", new BigDecimal("13000"), 160));
        data.add(new MonthlyDataDto("Apr", new BigDecimal("18000"), 210));
        data.add(new MonthlyDataDto("May", new BigDecimal("22000"), 250));
        data.add(new MonthlyDataDto("Jun", new BigDecimal("25000"), 300));
        data.add(new MonthlyDataDto("Jul", new BigDecimal("21000"), 260));
        data.add(new MonthlyDataDto("Aug", new BigDecimal("26000"), 320));
        data.add(new MonthlyDataDto("Sep", new BigDecimal("28000"), 350));
        data.add(new MonthlyDataDto("Oct", new BigDecimal("32000"), 400));
        data.add(new MonthlyDataDto("Nov", new BigDecimal("45000"), 550));
        data.add(new MonthlyDataDto("Dec", new BigDecimal("50000"), 600));
        return data;
    }

    public List<CategoryDataDto> getCategoryWiseData() {
        List<CategoryDataDto> data = new ArrayList<>();
        data.add(new CategoryDataDto("Technology", new BigDecimal("150000"), 45));
        data.add(new CategoryDataDto("Health", new BigDecimal("85000"), 30));
        data.add(new CategoryDataDto("Education", new BigDecimal("120000"), 50));
        data.add(new CategoryDataDto("Environment", new BigDecimal("60000"), 20));
        return data;
    }

    public List<TopDonorDto> getTopDonors() {
        List<TopDonorDto> data = new ArrayList<>();
        data.add(new TopDonorDto("Alice Smith", "alice@example.com", new BigDecimal("15000"), 12));
        data.add(new TopDonorDto("Bob Jones", "bob@example.com", new BigDecimal("12500"), 8));
        data.add(new TopDonorDto("Charlie Brown", "charlie@example.com", new BigDecimal("10000"), 5));
        return data;
    }
}
