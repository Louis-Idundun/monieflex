package com.sq018.monieflex.utils;

import com.sq018.monieflex.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserUtil {
    private final WalletRepository walletRepository;

    public static String getLoginUser (){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public boolean isBalanceSufficient(BigDecimal amount) {
        var loggedInUser = UserUtil.getLoginUser();
        var wallet = walletRepository.findByUser_EmailAddressIgnoreCase(loggedInUser);
        return wallet.isPresent() &&
                wallet.get().getBalance().compareTo(amount) > 0;
    }

    public void updateWalletBalance(BigDecimal amount, boolean isDebit) {
        var loggedInUser = UserUtil.getLoginUser();
        var wallet = walletRepository.findByUser_EmailAddressIgnoreCase(loggedInUser);
        if(wallet.isPresent()) {
            var walletValue = wallet.get();
            if(isDebit) {
                walletValue.setBalance(walletValue.getBalance().subtract(amount));
            } else {
                walletValue.setBalance(walletValue.getBalance().add(amount));
            }
            walletRepository.save(walletValue);
        }
    }
}
