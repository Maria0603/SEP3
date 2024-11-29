package com.example.data_server.repository;

import com.example.shared.dao.usersDao.BusinessDao;
import com.example.shared.dao.usersDao.CustomerDao;
import com.example.shared.dao.usersDao.UserDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserRepositoryImpl
{
  private final CustomerRepository customerRepository;
  private final BusinessRepository businessRepository;

  public UserRepositoryImpl(CustomerRepository customerRepository,
      BusinessRepository businessRepository)
  {
    this.customerRepository = customerRepository;
    this.businessRepository = businessRepository;
  }

  // Find a user by email
  public UserDao findUserByEmail(String email)
  {
    Optional<CustomerDao> customer = customerRepository.findByEmail(email);
    if (customer.isPresent())
    {
      return customer.get(); // Found in customers
    }

    Optional<BusinessDao> business = businessRepository.findByEmail(email);
    if (business.isPresent())
    {
      return business.get(); // Found in businesses
    }

    throw new IllegalArgumentException("User not found");
  }

  // Find a user by ID
  public UserDao findUserById(String id)
  {
    Optional<CustomerDao> customer = customerRepository.findById(id);
    if (customer.isPresent())
    {
      return customer.get(); // Found in customers
    }

    Optional<BusinessDao> business = businessRepository.findById(id);
    if (business.isPresent())
    {
      return business.get(); // Found in businesses
    }

    throw new IllegalArgumentException("User not found");
  }

  // Retrieve all users from both collections
  public List<UserDao> findAllUsers()
  {
    List<CustomerDao> customers = customerRepository.findAll();
    List<BusinessDao> businesses = businessRepository.findAll();

    return Stream.concat(customers.stream(), businesses.stream())
        .collect(Collectors.toList());
  }

  // Update user by email
  public void updateUser(String email, UserDao updatedData)
  {
    if (updatedData instanceof CustomerDao)
    {
      CustomerDao existingCustomer = (CustomerDao) findUserByEmail(email);
      CustomerDao updatedCustomer = (CustomerDao) updatedData;

      // Update fields
      existingCustomer.setFirstName(updatedCustomer.getFirstName());
      existingCustomer.setLastName(updatedCustomer.getLastName());
      existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

      customerRepository.save(existingCustomer);
    }
    else if (updatedData instanceof BusinessDao)
    {
      BusinessDao existingBusiness = (BusinessDao) findUserByEmail(email);
      BusinessDao updatedBusiness = (BusinessDao) updatedData;

      // Update fields
      existingBusiness.setBusinessName(updatedBusiness.getBusinessName());
      existingBusiness.setCvr(updatedBusiness.getCvr());
      existingBusiness.setPhoneNumber(updatedBusiness.getPhoneNumber());
      existingBusiness.setAddress(updatedBusiness.getAddress());

      businessRepository.save(existingBusiness);
    }
    else throw new IllegalArgumentException("Invalid user type");

  }

  // Delete a user by email
  public void deleteUserByEmail(String email)
  {
    if (customerRepository.findByEmail(email).isPresent())
    {
      customerRepository.deleteByEmail(email);
    }
    else if (businessRepository.findByEmail(email).isPresent())
    {
      businessRepository.deleteByEmail(email);
    }
    else
    {
      throw new IllegalArgumentException("User not found");
    }
  }

  // Delete all users
  public void deleteAllUsers()
  {
    customerRepository.deleteAll();
    businessRepository.deleteAll();
  }

  // Get statistics about user types
  public Map<String, Long> getUserStatistics()
  {
    long customerCount = customerRepository.count();
    long businessCount = businessRepository.count();

    Map<String, Long> stats = new HashMap<>();
    stats.put("customers", customerCount);
    stats.put("businesses", businessCount);
    stats.put("totalUsers", customerCount + businessCount);

    return stats;
  }

  // Example use case: Fetch user details
  public String getUserDetails(String email)
  {
    UserDao user = findUserByEmail(email);

    if (user instanceof CustomerDao)
    {
      CustomerDao customer = (CustomerDao) user;
      return "Customer: " + customer.getFirstName() + " "
          + customer.getLastName();
    }
    else if (user instanceof BusinessDao)
    {
      BusinessDao business = (BusinessDao) user;
      return "Business: " + business.getBusinessName() + ", CVR: "
          + business.getCvr();
    }

    throw new IllegalStateException("Unknown user type");
  }
}
