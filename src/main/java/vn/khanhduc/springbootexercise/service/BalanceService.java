package vn.khanhduc.springbootexercise.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.khanhduc.springbootexercise.repository.BalanceRepository;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "BALANCE-SERVICE")
public class BalanceService {

    private final BalanceRepository balanceRepository;
}
