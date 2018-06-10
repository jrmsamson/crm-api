package repositories.impl;

import exceptions.CustomerWithSameNameAndSurnameAlreadyExistException;
import model.entities.responses.CustomerResponse;
import model.pojos.Customer;
import org.jooq.Record4;
import org.jooq.SelectJoinStep;
import org.jooq.exception.DataAccessException;
import repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static model.jooq.Tables.CUSTOMER;

public class CustomerRepositoryImpl extends BaseRepositoryImpl implements CustomerRepository {

    private static final CharSequence CUSTOMER_NAME_SURNAME_DB_CONSTRAINT = "customer_name_surname_uindex";

    public Optional<CustomerResponse> getCustomerByUuid(UUID uuid) {
        return selectCustomer()
                .where(CUSTOMER.UUID.eq(uuid)
                        .and(CUSTOMER.ACTIVE.eq(Boolean.TRUE))
                )
                .fetchOptionalInto(CustomerResponse.class);
    }

    public List<CustomerResponse> getCustomersActive() {
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
                        CUSTOMER.PHOTO_NAME
                )
                .from(CUSTOMER);
    }

    public UUID addCustomer(Customer customer) {
        try {
            return doAddCustomer(customer);
        } catch (DataAccessException exception) {
            if (isConstraintUserAndSurnameUniqueException(exception))
                throw new CustomerWithSameNameAndSurnameAlreadyExistException();
            throw exception;
        }
    }

    private UUID doAddCustomer(Customer customer) {
        return create.insertInto(CUSTOMER)
                .set(create.newRecord(CUSTOMER, customer))
                .returning(CUSTOMER.UUID)
                .fetchOne()
                .getUuid();
    }

    private boolean isConstraintUserAndSurnameUniqueException(DataAccessException exception) {
        return exception.getMessage().contains(CUSTOMER_NAME_SURNAME_DB_CONSTRAINT);
    }

    public void editCustomerByUuid(Customer customer) {
        create.update(CUSTOMER)
                .set(CUSTOMER.NAME, customer.getName())
                .set(CUSTOMER.SURNAME, customer.getSurname())
                .set(CUSTOMER.MODIFIED_BY, customer.getModifiedBy())
                .where(CUSTOMER.UUID.eq(customer.getUuid()))
                .execute();
    }

    public void deleteCustomerUuid(UUID uuid) {
        create.update(CUSTOMER)
                .set(CUSTOMER.ACTIVE, Boolean.FALSE)
                .execute();
    }

    public void updateCustomerPhotoName(Customer customer) {
        create.update(CUSTOMER)
                .set(CUSTOMER.PHOTO_NAME, customer.getPhotoName())
                .where(CUSTOMER.UUID.eq(customer.getUuid()))
                .execute();
    }
}
