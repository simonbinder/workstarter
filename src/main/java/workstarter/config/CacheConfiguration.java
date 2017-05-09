package workstarter.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(workstarter.domain.Student.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(workstarter.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.CompanyAdmin.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.Offering.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.Student.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(workstarter.domain.CompanyAdmin.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(workstarter.domain.Student.class.getName() + ".offeringValues", jcacheConfiguration);
            cm.createCache(workstarter.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.Student.class.getName() + ".searchingValues", jcacheConfiguration);
            cm.createCache(workstarter.domain.Student.class.getName() + ".resumes", jcacheConfiguration);
            cm.createCache(workstarter.domain.Company.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.Company.class.getName() + ".jobadvertisments", jcacheConfiguration);
            cm.createCache(workstarter.domain.Company.class.getName() + ".admins", jcacheConfiguration);
            cm.createCache(workstarter.domain.CompanyAdmin.class.getName() + ".companies", jcacheConfiguration);
            cm.createCache(workstarter.domain.Jobadvertisment.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.Portfolio.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.Qualification.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.Resume.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.Resume.class.getName() + ".schools", jcacheConfiguration);
            cm.createCache(workstarter.domain.Resume.class.getName() + ".companies", jcacheConfiguration);
            cm.createCache(workstarter.domain.Resume.class.getName() + ".internships", jcacheConfiguration);
            cm.createCache(workstarter.domain.Resume.class.getName() + ".qualifications", jcacheConfiguration);
            cm.createCache(workstarter.domain.School.class.getName(), jcacheConfiguration);
            cm.createCache(workstarter.domain.Searching.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
