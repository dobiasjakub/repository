package com.jddev.springeshop.service.impl;

import com.jddev.springeshop.persistance.entity.Device;
import com.jddev.springeshop.persistance.repository.DeviceRepository;
import com.jddev.springeshop.service.DeviceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device saveDevice(Device device) {
        device.setCreated(LocalDateTime.now());

        return deviceRepository.save(device);
    }

    @Override
    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    @Override
    public List<Device> findAllDevices() {
        return deviceRepository.findAll();
    }
}

