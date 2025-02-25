package com.example.the_java_bank.service;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.the_java_bank.dto.RequestDTO.AccountInfo;
import com.example.the_java_bank.dto.RequestDTO.CreditRequest;
import com.example.the_java_bank.dto.RequestDTO.DebitRequest;
import com.example.the_java_bank.dto.RequestDTO.EmailDetail;
import com.example.the_java_bank.dto.RequestDTO.TransactionDTO;
import com.example.the_java_bank.dto.RequestDTO.TransferRequest;
import com.example.the_java_bank.dto.RequestDTO.UserRequest;
import com.example.the_java_bank.dto.ResponseDTO.BankResponse;
import com.example.the_java_bank.entity.User;
import com.example.the_java_bank.repository.UserRepository;
import com.example.the_java_bank.service.impl.EmailService;
import com.example.the_java_bank.service.impl.TransactionService;
import com.example.the_java_bank.service.impl.UserService;
import com.example.the_java_bank.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;

        private final EmailService emailService;

        private final TransactionService transactionService;

        private final PasswordEncoder passwordEncoder;

        public UserServiceImpl(UserRepository userRepository, EmailService emailService,
                        TransactionService transactionService, @Lazy PasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.emailService = emailService;
                this.transactionService = transactionService;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        public UserDetailsService userDetailsService() {
                return email -> userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        @Override
        public BankResponse creatAccount(UserRequest userRequest) {

                /*
                 * Creating an account - saving a new user into the database
                 * Check if user already has an account
                 */

                if (userRepository.existsByEmail(userRequest.getEmail())) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                User newUser = User.builder()
                                .firstName(userRequest.getFirstName())
                                .lastName(userRequest.getLastName())
                                .otherName(userRequest.getOtherName())
                                .gender(userRequest.getGender())
                                .address(userRequest.getAddress())
                                .stateOfOrigin(userRequest.getStateOfOrigin())
                                .accountNumber(AccountUtils.generateAccountNumber())
                                .accountBalance(BigDecimal.ZERO)
                                .email(userRequest.getEmail())
                                .password(passwordEncoder.encode(userRequest.getPassword()))
                                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                                .phoneNumber(userRequest.getPhoneNumber())
                                .status("ACTIVE")
                                .build();

                User saveUser = userRepository.save(newUser);

                String messageMail = "Congratulations !, Your Account Has Been Sucessfully Created.\nYour Account Details:\n"
                                +
                                "Account Name: " + saveUser.getLastName() + " " + saveUser.getFirstName() + " "
                                + saveUser.getOtherName()
                                + "\nAcount Number: " + saveUser.getAccountNumber();

                EmailDetail emailDetail = EmailDetail.builder()
                                .recipient(saveUser.getEmail())
                                .messageBody(messageMail)
                                .subject("ACCOUNT CREATION")
                                .build();

                emailService.sendEmail(emailDetail);

                return BankResponse.builder()
                                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(saveUser.getAccountBalance())
                                                .accountName(
                                                                saveUser.getLastName() + " " + saveUser.getFirstName()
                                                                                + " " + saveUser.getOtherName())
                                                .accountNumber(saveUser.getAccountNumber())
                                                .build())
                                .build();

        }

        // balance enquiry, name enquiry, credit, debit, transfer

        @Override
        public BankResponse balanceEnquiry(String accountNumber) {

                boolean isAccountNumber = userRepository.existsByAccountNumber(accountNumber);

                if (!isAccountNumber) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                User foundUser = userRepository.findByAccountNumber(accountNumber);

                return BankResponse.builder()
                                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(foundUser.getAccountBalance())
                                                .accountName(
                                                                foundUser.getLastName() + " " + foundUser.getFirstName()
                                                                                + " " + foundUser.getOtherName())
                                                .accountNumber(foundUser.getAccountNumber())
                                                .build())
                                .build();

        }

        @Override
        public String nameEnquiry(String accountNumber) {

                boolean isAccountNumber = userRepository.existsByAccountNumber(accountNumber);

                if (!isAccountNumber) {
                        return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
                }

                User foundUser = userRepository.findByAccountNumber(accountNumber);

                return foundUser.getLastName() + " " + foundUser.getFirstName()
                                + " " + foundUser.getOtherName();

        }

        @Override
        public BankResponse credit(String accountNumber, CreditRequest creditDebitRequest) {

                boolean isAccountNumber = userRepository.existsByAccountNumber(accountNumber);

                if (!isAccountNumber) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                User userToCredit = userRepository.findByAccountNumber(accountNumber);

                userToCredit.setAccountBalance(
                                userToCredit.getAccountBalance().add(creditDebitRequest.getCreditAmount()));
                userRepository.save(userToCredit);

                TransactionDTO transactionDTO = TransactionDTO.builder()
                                .accountNumber(userToCredit.getAccountNumber())
                                .amount(creditDebitRequest.getCreditAmount())
                                .transactionType("CREDIT")
                                .build();

                transactionService.saveTransaction(transactionDTO);

                return BankResponse.builder()
                                .responseCode(AccountUtils.ACCOUNT_CREDIT_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_CREDIT_MESSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(userToCredit.getAccountBalance())
                                                .accountName(
                                                                userToCredit.getLastName() + " "
                                                                                + userToCredit.getFirstName()
                                                                                + " " + userToCredit.getOtherName())
                                                .accountNumber(userToCredit.getAccountNumber())
                                                .build())
                                .build();
        }

        @Override
        public BankResponse debit(String accountNumber, DebitRequest debitRequest) {

                boolean isAccountNumber = userRepository.existsByAccountNumber(accountNumber);

                if (!isAccountNumber) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                User user = userRepository.findByAccountNumber(accountNumber);

                BigInteger availableBalance = user.getAccountBalance().toBigInteger();
                BigInteger debitAmount = debitRequest.getDebitAmount().toBigInteger();

                if (availableBalance.intValue() < debitAmount.intValue()) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                                        .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                } else {
                        user.setAccountBalance(user.getAccountBalance().subtract(debitRequest.getDebitAmount()));
                        userRepository.save(user);

                        TransactionDTO transactionDTO = TransactionDTO.builder()
                                        .accountNumber(user.getAccountNumber())
                                        .amount(debitRequest.getDebitAmount())
                                        .transactionType("DEBIT")
                                        .build();

                        transactionService.saveTransaction(transactionDTO);

                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                                        .accountInfo(AccountInfo.builder()
                                                        .accountBalance(user.getAccountBalance())
                                                        .accountName(
                                                                        user.getLastName() + " "
                                                                                        + user.getFirstName()
                                                                                        + " "
                                                                                        + user.getOtherName())
                                                        .accountNumber(user.getAccountNumber())
                                                        .build())
                                        .build();

                }

        }

        @Override
        @Transactional
        public BankResponse transfer(TransferRequest transferRequest) {

                // Kiểm tra tài khoản đích có tồn tại không
                boolean isDestinationAccount = userRepository
                                .existsByAccountNumber(transferRequest.getDestinationAccountNumber());

                if (!isDestinationAccount) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                // Kiểm tra nếu tài khoản nguồn và tài khoản đích trùng nhau
                if (transferRequest.getSourceAccountNumber().equals(transferRequest.getDestinationAccountNumber())) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.INVALID_TRANSFER_CODE) // Tạo mã lỗi riêng
                                        .responseMessage(AccountUtils.INVALID_TRANSFER_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                // Lấy thông tin tài khoản nguồn
                User accountUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());

                // Kiểm tra số dư có đủ không
                if (transferRequest.getAmount().compareTo(accountUser.getAccountBalance()) > 0) {
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                                        .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                // Trừ tiền tài khoản nguồn
                accountUser.setAccountBalance(accountUser.getAccountBalance().subtract(transferRequest.getAmount()));
                userRepository.save(accountUser);

                // Lưu giao dịch debit (ghi nợ) của tài khoản nguồn
                TransactionDTO transactionDebit = TransactionDTO.builder()
                                .accountNumber(accountUser.getAccountNumber())
                                .amount(transferRequest.getAmount())
                                .transactionType("DEBIT")
                                .build();
                transactionService.saveTransaction(transactionDebit);

                // Lấy thông tin tài khoản đích
                User destinationAccount = userRepository
                                .findByAccountNumber(transferRequest.getDestinationAccountNumber());

                // Cộng tiền tài khoản đích
                destinationAccount.setAccountBalance(
                                destinationAccount.getAccountBalance().add(transferRequest.getAmount()));
                userRepository.save(destinationAccount);

                // Lưu giao dịch credit (ghi có) của tài khoản đích
                TransactionDTO transactionCredit = TransactionDTO.builder()
                                .accountNumber(destinationAccount.getAccountNumber())
                                .amount(transferRequest.getAmount())
                                .transactionType("CREDIT")
                                .build();

                transactionService.saveTransaction(transactionCredit);

                // Gửi email thông báo cho tài khoản nguồn
                EmailDetail debitAlert = EmailDetail.builder()
                                .recipient(accountUser.getEmail())
                                .messageBody("The sum of " + transferRequest.getAmount()
                                                + " has been deducted from your account. Your current balance is "
                                                + accountUser.getAccountBalance() + ".")
                                .subject("DEBIT ALERT")
                                .build();
                emailService.sendEmail(debitAlert);

                // Gửi email thông báo cho tài khoản đích
                EmailDetail creditAlert = EmailDetail.builder()
                                .recipient(destinationAccount.getEmail())
                                .messageBody("The sum of " + transferRequest.getAmount()
                                                + " has been sent to your account. Your current balance is "
                                                + destinationAccount.getAccountBalance() + ".")
                                .subject("CREDIT ALERT")
                                .build();
                emailService.sendEmail(creditAlert);

                return BankResponse.builder()
                                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                                .accountInfo(null)
                                .build();
        }

}
