package repositories.impl;

import model.entities.CustomerResponse;
import model.pojos.Customer;
import org.jooq.Record4;
import org.jooq.SelectJoinStep;
import repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static model.jooq.Tables.CUSTOMER;

public class CustomerRepositoryImpl extends BaseRepositoryImpl implements CustomerRepository {

    public Optional<CustomerResponse> getCustomerByUuid(UUID uuid) {
        return selectCustomer()
                .where(CUSTOMER.UUID.eq(uuid))
                .fetchOptionalInto(CustomerResponse.class);
    }

    public List<CustomerResponse> getAllCustomerActive() {
        return selectCustomer()
                .where(CUSTOMER.ACTIVE.eq(Boolean.TRUE))
                .orderBy(CUSTOMER.ID.asc())
                .fetchInto(CustomerResponse.class);
    }

    private SelectJoinStep<Record4<UUID, String, String, String>> selectCustomer() {
        return create
                .select(
                        CUSTOMER.UUID,
                        CUSTOMER.NAME,
                        CUSTOMER.SURNAME,
                        CUSTOMER.PHOTO_URL
                )
                .from(CUSTOMER);
    }

    public void addCustomer(Customer customer) {
        create.insertInto(CUSTOMER)
                .set(create.newRecord(CUSTOMER, customer))
                .execute();
    }

    public void editCustomer(Customer customer) {
        create.update(CUSTOMER)
                .set(CUSTOMER.NAME, customer.getName())
                .set(CUSTOMER.SURNAME, customer.getSurname())
                .set(CUSTOMER.MODIFIED_BY, customer.getModifiedBy())
                .where(CUSTOMER.UUID.eq(customer.getUuid()))
                .execute();
    }

    public void deleteCustomer(UUID uuid) {
        create.update(CUSTOMER)
                .set(CUSTOMER.ACTIVE, Boolean.FALSE)
                .execute();
    }

    @Override
    public void updateCustomerPhotoName(Customer customer) {
        create.update(CUSTOMER)
                .set(CUSTOMER.PHOTO_URL, customer.getPhotoUrl())
                .where(CUSTOMER.UUID.eq(customer.getUuid()))
                .execute();
    }
}
