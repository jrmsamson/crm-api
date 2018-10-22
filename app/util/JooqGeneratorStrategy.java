package util;

import com.google.common.base.Joiner;
import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;

public class JooqGeneratorStrategy extends DefaultGeneratorStrategy {

    private static final String MODEL_PACKAGE_NAME = "model";
    private static final String POJOS_PACKAGE_NAME = "pojos";
    private static final String PACKAGE_SEPARATOR = ".";

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        String packageName = super.getJavaPackageName(definition, mode);

        if (isPojosPackage(packageName))
            return Joiner.on(PACKAGE_SEPARATOR).join(MODEL_PACKAGE_NAME, POJOS_PACKAGE_NAME);

        return MODEL_PACKAGE_NAME + packageName.substring(5);
    }

    private boolean isPojosPackage(String packageName) {
        return packageName.endsWith(POJOS_PACKAGE_NAME);
    }

}
