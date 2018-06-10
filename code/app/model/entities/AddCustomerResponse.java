package model.entities;

import java.util.UUID;

public class AddCustomerResponse {

    private UUID customerUuid;

    public AddCustomerResponse() {
    }

    public AddCustomerResponse(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }
}
