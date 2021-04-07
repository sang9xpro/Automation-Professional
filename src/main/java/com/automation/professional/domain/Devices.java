package com.automation.professional.domain;

import com.automation.professional.domain.enumeration.DeviceStatus;
import com.automation.professional.domain.enumeration.DeviceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Devices.
 */
@Entity
@Table(name = "devices")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "devices")
public class Devices implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "os")
    private String os;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type")
    private DeviceType deviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatus status;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @Column(name = "owner")
    private String owner;

    @JsonIgnoreProperties(value = { "devices", "accounts", "facebook" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Country country;

    @OneToMany(mappedBy = "devices")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "devices", "devicesFields" }, allowSetters = true)
    private Set<DeviceValues> deviceValues = new HashSet<>();

    @OneToMany(mappedBy = "devices")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "loggersValues", "devices" }, allowSetters = true)
    private Set<Loggers> loggers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_devices__accounts",
        joinColumns = @JoinColumn(name = "devices_id"),
        inverseJoinColumns = @JoinColumn(name = "accounts_id")
    )
    @JsonIgnoreProperties(value = { "country", "accountValues", "devices" }, allowSetters = true)
    private Set<Accounts> accounts = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_devices__scheduler_task_device",
        joinColumns = @JoinColumn(name = "devices_id"),
        inverseJoinColumns = @JoinColumn(name = "scheduler_task_device_id")
    )
    @JsonIgnoreProperties(value = { "schedulerTaskDeviceValues", "schedulers", "tasks", "devices" }, allowSetters = true)
    private Set<SchedulerTaskDevice> schedulerTaskDevices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Devices id(Long id) {
        this.id = id;
        return this;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public Devices ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public Devices macAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getOs() {
        return this.os;
    }

    public Devices os(String os) {
        this.os = os;
        return this;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public Devices deviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceStatus getStatus() {
        return this.status;
    }

    public Devices status(DeviceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public Devices lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getOwner() {
        return this.owner;
    }

    public Devices owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Country getCountry() {
        return this.country;
    }

    public Devices country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<DeviceValues> getDeviceValues() {
        return this.deviceValues;
    }

    public Devices deviceValues(Set<DeviceValues> deviceValues) {
        this.setDeviceValues(deviceValues);
        return this;
    }

    public Devices addDeviceValues(DeviceValues deviceValues) {
        this.deviceValues.add(deviceValues);
        deviceValues.setDevices(this);
        return this;
    }

    public Devices removeDeviceValues(DeviceValues deviceValues) {
        this.deviceValues.remove(deviceValues);
        deviceValues.setDevices(null);
        return this;
    }

    public void setDeviceValues(Set<DeviceValues> deviceValues) {
        if (this.deviceValues != null) {
            this.deviceValues.forEach(i -> i.setDevices(null));
        }
        if (deviceValues != null) {
            deviceValues.forEach(i -> i.setDevices(this));
        }
        this.deviceValues = deviceValues;
    }

    public Set<Loggers> getLoggers() {
        return this.loggers;
    }

    public Devices loggers(Set<Loggers> loggers) {
        this.setLoggers(loggers);
        return this;
    }

    public Devices addLoggers(Loggers loggers) {
        this.loggers.add(loggers);
        loggers.setDevices(this);
        return this;
    }

    public Devices removeLoggers(Loggers loggers) {
        this.loggers.remove(loggers);
        loggers.setDevices(null);
        return this;
    }

    public void setLoggers(Set<Loggers> loggers) {
        if (this.loggers != null) {
            this.loggers.forEach(i -> i.setDevices(null));
        }
        if (loggers != null) {
            loggers.forEach(i -> i.setDevices(this));
        }
        this.loggers = loggers;
    }

    public Set<Accounts> getAccounts() {
        return this.accounts;
    }

    public Devices accounts(Set<Accounts> accounts) {
        this.setAccounts(accounts);
        return this;
    }

    public Devices addAccounts(Accounts accounts) {
        this.accounts.add(accounts);
        accounts.getDevices().add(this);
        return this;
    }

    public Devices removeAccounts(Accounts accounts) {
        this.accounts.remove(accounts);
        accounts.getDevices().remove(this);
        return this;
    }

    public void setAccounts(Set<Accounts> accounts) {
        this.accounts = accounts;
    }

    public Set<SchedulerTaskDevice> getSchedulerTaskDevices() {
        return this.schedulerTaskDevices;
    }

    public Devices schedulerTaskDevices(Set<SchedulerTaskDevice> schedulerTaskDevices) {
        this.setSchedulerTaskDevices(schedulerTaskDevices);
        return this;
    }

    public Devices addSchedulerTaskDevice(SchedulerTaskDevice schedulerTaskDevice) {
        this.schedulerTaskDevices.add(schedulerTaskDevice);
        schedulerTaskDevice.getDevices().add(this);
        return this;
    }

    public Devices removeSchedulerTaskDevice(SchedulerTaskDevice schedulerTaskDevice) {
        this.schedulerTaskDevices.remove(schedulerTaskDevice);
        schedulerTaskDevice.getDevices().remove(this);
        return this;
    }

    public void setSchedulerTaskDevices(Set<SchedulerTaskDevice> schedulerTaskDevices) {
        this.schedulerTaskDevices = schedulerTaskDevices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Devices)) {
            return false;
        }
        return id != null && id.equals(((Devices) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Devices{" +
            "id=" + getId() +
            ", ipAddress='" + getIpAddress() + "'" +
            ", macAddress='" + getMacAddress() + "'" +
            ", os='" + getOs() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", status='" + getStatus() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
