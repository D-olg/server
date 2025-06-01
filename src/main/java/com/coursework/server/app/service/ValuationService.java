package com.coursework.server.app.service;
import com.coursework.server.app.model.DiscountRate;
import com.coursework.server.app.model.FinancialData;
import com.coursework.server.app.model.Valuation;
import com.coursework.server.app.repository.ValuationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ValuationService {

    private final ValuationRepository valuationRepository;

    public ValuationService(ValuationRepository valuationRepository) {
        this.valuationRepository = valuationRepository;
    }

    @Transactional
    public Valuation save(Valuation valuation) {
        return valuationRepository.save(valuation);
    }

    public BigDecimal calculateEboValuation(List<FinancialData> financialDataList, List<DiscountRate> discountRates) {
        Map<Integer, FinancialData> yearToData = new HashMap<>();
        for (FinancialData data : financialDataList) {
            yearToData.put(data.getYear(), data);
        }

        Map<Integer, BigDecimal> yearToRate = new HashMap<>();
        for (DiscountRate rate : discountRates) {
            yearToRate.put(rate.getYear(), rate.getRate().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)); // Преобразуем проценты в доли
        }

        // Сортировка годов
        List<Integer> years = new ArrayList<>(yearToData.keySet());
        Collections.sort(years);

        if (years.isEmpty()) return BigDecimal.ZERO;

        int baseYear = years.getFirst();
        BigDecimal Bt = yearToData.get(baseYear).getEquity();
        BigDecimal sum = BigDecimal.ZERO;

        System.out.println("Базовый год: " + baseYear);
        System.out.println("Начальная база (Bt): " + Bt);

        for (int i = 1; i < years.size(); i++) {
            int prevYear = years.get(i - 1);
            int currYear = years.get(i);

            FinancialData previous = yearToData.get(prevYear);
            FinancialData current = yearToData.get(currYear);

            BigDecimal r = yearToRate.getOrDefault(currYear, BigDecimal.valueOf(0.10)); // По умолчанию 10%

            System.out.println("Год: " + currYear);
            System.out.println("Ставка дисконтирования: " + r);

            BigDecimal deltaX = current.getNetIncome().subtract(r.multiply(previous.getEquity()));
            System.out.println("deltaX для года " + currYear + ": " + deltaX);

            int yearDiff = currYear - baseYear;
            BigDecimal denominator = BigDecimal.ONE.add(r).pow(yearDiff);
            System.out.println("Denominator для года " + currYear + ": " + denominator);

            BigDecimal discounted = deltaX.divide(denominator, 6, RoundingMode.HALF_UP);
            System.out.println("Discounted для года " + currYear + ": " + discounted);

            sum = sum.add(discounted);
            System.out.println("Сумма (sum) после года " + currYear + ": " + sum);
        }

        BigDecimal valuation = Bt.add(sum).setScale(2, RoundingMode.HALF_UP);
        System.out.println("Итоговая оценка: " + valuation);

        return valuation;
    }

    public List<Valuation> getValuationsByUser(Integer userId) {
        return valuationRepository.findAllByUserId(userId);
    }
}
