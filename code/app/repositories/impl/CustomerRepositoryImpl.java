package repositories.impl;

import exceptions.CustomerRepositoryException;
import model.pojos.Customer;
import repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static model.jooq.Tables.CUSTOMER;

public class CustomerRepositoryImpl extends BaseRepositoryImpl implements CustomerRepository {

    public Optional<Customer> getByUuid(UUID uuid) {
        return create
                .selectFrom(CUSTOMER)
                .where(CUSTOMER.UUID.eq(uuid)
                        .and(CUSTOMER.ACTIVE.eq(Boolean.TRUE))
                )
                .fetchOptionalInto(Customer.class);
    }

    public List<Customer> getActive() {
        return create
                .selectFrom(CUSTOMER)
                .where(CUSTOMER.ACTIVE.eq(Boolean.TRUE))
                .orderBy(CUSTOMER.ID.asc())
                .fetchInto(Customer.class);
    }

    public UUID add(Customer customer) {
        try {
            return doAdd(customer);
        } catch (Exception e) {
            throw new CustomerRepositoryException(e);
        }
    }

    private UUID doAdd(Customer customer) {
        return create.insertInto(CUSTOMER)
                .set(create.newRecord(CUSTOMER, customer))
                .returning(CUSTOMER.UUID)
                .fetchOne()
                .getUuid();
    }

    public void update(Customer customer) {
        create.update(CUSTOMER)
                .set(CUSTOMER.NAME, customer.getName())
                .set(CUSTOMER.SURNAME, customer.getSurname())
                .set(CUSTOMER.MODIFIED_BY, customer.getModifiedBy())
                .set(CUSTOMER.PHOTO_NAME, customer.getPhotoName())
                .where(CUSTOMER.UUID.eq(customer.getUuid()))
                .execute();
    }

    public void deleteByUuid(UUID uuid) {
        create.update(CUSTOMER)
                .set(CUSTOMER.ACTIVE, Boolean.FALSE)
                .execute();
    }

    public Optional<Customer> getByNameAndSurname(String name, String surname) {
        return create.selectFrom(CUSTOMER)
                .where(
                        CUSTOMER.NAME.eq(name)
                                .and(CUSTOMER.SURNAME.eq(surname))
                )
                .fetchOptionalInto(Customer.class);
    }

}
