package com.jddev.springeshop.service;

import com.jddev.springeshop.persistance.entity.Device;
import com.jddev.springeshop.persistance.entity.User;
import com.jddev.springeshop.persistance.enums.Role;
import com.jddev.springeshop.persistance.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DeviceService {

    Device saveDevice(Device device);

    void deleteDevice(Long id);

    List<Device> findAllDevices();
}
