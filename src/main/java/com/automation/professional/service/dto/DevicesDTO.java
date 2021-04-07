package com.automation.professional.service.dto;

import com.automation.professional.domain.enumeration.DeviceStatus;
import com.automation.professional.domain.enumeration.DeviceType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.automation.professional.domain.Devices} entity.
 */
public class DevicesDTO implements Serializable {

    private Long id;

    private String ipAddress;

    private String macAddress;

    private String os;

    private DeviceType deviceType;

    private DeviceStatus status;

    private Instant lastUpdate;

    private String owner;

    private CountryDTO country;

    private Set<AccountsDTO> accounts = new HashSet<>();

    private Set<SchedulerTaskDeviceDTO> schedulerTaskDevices = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    public Set<AccountsDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountsDTO> accounts) {
        this.accounts = accounts;
    }

    public Set<SchedulerTaskDeviceDTO> getSchedulerTaskDevices() {
        return schedulerTaskDevices;
    }

    public void setSchedulerTaskDevices(Set<SchedulerTaskDeviceDTO> schedulerTaskDevices) {
        this.schedulerTaskDevices = schedulerTaskDevices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DevicesDTO)) {
            return false;
        }

        DevicesDTO devicesDTO = (DevicesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, devicesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DevicesDTO{" +
            "id=" + getId() +
            ", ipAddress='" + getIpAddress() + "'" +
            ", macAddress='" + getMacAddress() + "'" +
            ", os='" + getOs() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", status='" + getStatus() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", owner='" + getOwner() + "'" +
            ", country=" + getCountry() +
            ", accounts=" + getAccounts() +
            ", schedulerTaskDevices=" + getSchedulerTaskDevices() +
            "}";
    }
}
