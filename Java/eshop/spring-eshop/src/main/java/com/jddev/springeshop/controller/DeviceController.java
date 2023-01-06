package com.jddev.springeshop.controller;

import com.jddev.springeshop.persistance.entity.Device;
import com.jddev.springeshop.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    public ResponseEntity<?> saveDevice(@RequestBody Device device) {
        deviceService.saveDevice(device);

        return new ResponseEntity<>(deviceService.saveDevice(device), HttpStatus.OK);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<?> saveDevice(@PathVariable Long deviceId) {
        deviceService.deleteDevice(deviceId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllDevices() {
        return new ResponseEntity<>(deviceService.findAllDevices(), HttpStatus.OK);
    }
}
