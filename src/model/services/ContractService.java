package model.services;

import model.entities.Contract;
import model.entities.Installment;

import java.time.LocalDate;

public class ContractService {

    private final OnlineContractService onlineContractService;

    public ContractService(final OnlineContractService onlineContractService) {
        this.onlineContractService = onlineContractService;
    }

    public void processContract(final Contract contract, final int months) {
        double basicQuota = contract.getTotalValue() / months;

        for (int i = 1; i <= months; i++) {
            LocalDate dueDate = contract.getDate().plusMonths(i);
            double quota = calculateQuota(basicQuota, i);

            contract.getInstallments().add(new Installment(dueDate, quota));
        }
    }

    private double calculateQuota(double basicQuota, int i) {
        double interest = onlineContractService.interest(basicQuota, i);
        double fee = onlineContractService.paymentFee(basicQuota + interest);

        return basicQuota + interest + fee;
    }
}
