package codes.bespoke.brastak.snippets.zero.config;

import org.togglz.core.Feature;
import org.togglz.core.annotation.FeatureGroup;
import org.togglz.core.annotation.Label;

public enum ZeroDTFeatures implements Feature {
    @FeatureGroup("Add status field")
    @Label("Write new status field only")
    FEATURE_WRITE_POST_STATUS_ONLY,

    @FeatureGroup("Add status field")
    @Label("Read new status field")
    FEATURE_READ_POST_STATUS
}
