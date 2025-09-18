package lk.ijse.drivingschool.service;

import lk.ijse.drivingschool.dto.VehicleDTO;

public interface VehicleService {
    Object getAvailableVehicles();
    Object getVehicleData();
    String updateVehicle(String updateVehicleNum, VehicleDTO vehicleDTO);
    String deleteVehicle(String vehicleNum);
    String saveVehicle(VehicleDTO vehicleDTO);
}
