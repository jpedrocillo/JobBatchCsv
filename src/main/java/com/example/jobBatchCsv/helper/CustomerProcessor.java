package com.example.jobBatchCsv.helper;

import com.example.jobBatchCsv.model.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer customer) throws Exception{
        //you can add condition here
        // if(customer.id <100){
        //	return customer;
        // }else {return null};

        return customer;


    }
}
