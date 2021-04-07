package com.automation.professional.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.automation.professional.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.automation.professional.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.automation.professional.domain.User.class.getName());
            createCache(cm, com.automation.professional.domain.Authority.class.getName());
            createCache(cm, com.automation.professional.domain.User.class.getName() + ".authorities");
            createCache(cm, com.automation.professional.domain.Loggers.class.getName());
            createCache(cm, com.automation.professional.domain.Loggers.class.getName() + ".loggersValues");
            createCache(cm, com.automation.professional.domain.LoggersFields.class.getName());
            createCache(cm, com.automation.professional.domain.LoggersFields.class.getName() + ".loggersValues");
            createCache(cm, com.automation.professional.domain.LoggersValues.class.getName());
            createCache(cm, com.automation.professional.domain.History.class.getName());
            createCache(cm, com.automation.professional.domain.History.class.getName() + ".historyValues");
            createCache(cm, com.automation.professional.domain.HistoryFields.class.getName());
            createCache(cm, com.automation.professional.domain.HistoryFields.class.getName() + ".historyValues");
            createCache(cm, com.automation.professional.domain.HistoryValues.class.getName());
            createCache(cm, com.automation.professional.domain.Comment.class.getName());
            createCache(cm, com.automation.professional.domain.Comment.class.getName() + ".commentValues");
            createCache(cm, com.automation.professional.domain.CommentFields.class.getName());
            createCache(cm, com.automation.professional.domain.CommentFields.class.getName() + ".commentValues");
            createCache(cm, com.automation.professional.domain.CommentValues.class.getName());
            createCache(cm, com.automation.professional.domain.Accounts.class.getName());
            createCache(cm, com.automation.professional.domain.Accounts.class.getName() + ".accountValues");
            createCache(cm, com.automation.professional.domain.Accounts.class.getName() + ".devices");
            createCache(cm, com.automation.professional.domain.AccountFields.class.getName());
            createCache(cm, com.automation.professional.domain.AccountFields.class.getName() + ".accountValues");
            createCache(cm, com.automation.professional.domain.AccountValues.class.getName());
            createCache(cm, com.automation.professional.domain.Schedulers.class.getName());
            createCache(cm, com.automation.professional.domain.Schedulers.class.getName() + ".schedulerValues");
            createCache(cm, com.automation.professional.domain.Schedulers.class.getName() + ".schedulerTaskDevices");
            createCache(cm, com.automation.professional.domain.SchedulerFields.class.getName());
            createCache(cm, com.automation.professional.domain.SchedulerFields.class.getName() + ".schedulerValues");
            createCache(cm, com.automation.professional.domain.SchedulerValue.class.getName());
            createCache(cm, com.automation.professional.domain.Tasks.class.getName());
            createCache(cm, com.automation.professional.domain.Tasks.class.getName() + ".taskValues");
            createCache(cm, com.automation.professional.domain.Tasks.class.getName() + ".schedulerTaskDevices");
            createCache(cm, com.automation.professional.domain.TaskFields.class.getName());
            createCache(cm, com.automation.professional.domain.TaskFields.class.getName() + ".taskValues");
            createCache(cm, com.automation.professional.domain.TaskValues.class.getName());
            createCache(cm, com.automation.professional.domain.Devices.class.getName());
            createCache(cm, com.automation.professional.domain.Devices.class.getName() + ".deviceValues");
            createCache(cm, com.automation.professional.domain.Devices.class.getName() + ".loggers");
            createCache(cm, com.automation.professional.domain.Devices.class.getName() + ".accounts");
            createCache(cm, com.automation.professional.domain.Devices.class.getName() + ".schedulerTaskDevices");
            createCache(cm, com.automation.professional.domain.DevicesFields.class.getName());
            createCache(cm, com.automation.professional.domain.DevicesFields.class.getName() + ".deviceValues");
            createCache(cm, com.automation.professional.domain.DeviceValues.class.getName());
            createCache(cm, com.automation.professional.domain.SchedulerTaskDevice.class.getName());
            createCache(cm, com.automation.professional.domain.SchedulerTaskDevice.class.getName() + ".schedulerTaskDeviceValues");
            createCache(cm, com.automation.professional.domain.SchedulerTaskDevice.class.getName() + ".devices");
            createCache(cm, com.automation.professional.domain.SchedulerTaskDeviceFields.class.getName());
            createCache(cm, com.automation.professional.domain.SchedulerTaskDeviceFields.class.getName() + ".schedulerTaskDeviceValues");
            createCache(cm, com.automation.professional.domain.SchedulerTaskDeviceValues.class.getName());
            createCache(cm, com.automation.professional.domain.Facebook.class.getName());
            createCache(cm, com.automation.professional.domain.Facebook.class.getName() + ".facebookValues");
            createCache(cm, com.automation.professional.domain.Facebook.class.getName() + ".mostOfContents");
            createCache(cm, com.automation.professional.domain.FacebookFields.class.getName());
            createCache(cm, com.automation.professional.domain.FacebookFields.class.getName() + ".facebookValues");
            createCache(cm, com.automation.professional.domain.FacebookValues.class.getName());
            createCache(cm, com.automation.professional.domain.MostOfContent.class.getName());
            createCache(cm, com.automation.professional.domain.MostOfContent.class.getName() + ".mostOfContValues");
            createCache(cm, com.automation.professional.domain.MostOfContent.class.getName() + ".comments");
            createCache(cm, com.automation.professional.domain.MostOfContFields.class.getName());
            createCache(cm, com.automation.professional.domain.MostOfContFields.class.getName() + ".mostOfContValues");
            createCache(cm, com.automation.professional.domain.MostOfContValues.class.getName());
            createCache(cm, com.automation.professional.domain.Country.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
