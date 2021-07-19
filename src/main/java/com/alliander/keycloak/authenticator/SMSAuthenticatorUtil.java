package com.alliander.keycloak.authenticator;

import java.util.Comparator;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.UserModel;

/**
 * Created by joris on 18/11/2016.
 */
public class SMSAuthenticatorUtil {

    private static Logger logger = Logger.getLogger(SMSAuthenticatorUtil.class);

    public static String getAttributeValue(UserModel user, String attributeName) {
        return user.getAttributeStream(attributeName).findFirst()
            .orElse(null);
    }


    public static String getCredentialValue(AuthenticationFlowContext context, String credentialName) {
        return context.getSession().userCredentialManager()
            .getStoredCredentialsStream(context.getRealm(), context.getUser())
            .filter(credentialModel -> credentialModel.getType().equals(credentialName))
            .max(Comparator.comparing(CredentialModel::getCreatedDate))
        .map(credentialModel -> credentialModel.getValue())
        .orElse(null);
    }

    public static String getConfigString(AuthenticatorConfigModel config, String configName) {
        return getConfigString(config, configName, null);
    }

    public static String getConfigString(AuthenticatorConfigModel config, String configName, String defaultValue) {

        String value = defaultValue;

        if (config.getConfig() != null) {
            // Get value
            value = config.getConfig().get(configName);
        }

        return value;
    }

    public static Long getConfigLong(AuthenticatorConfigModel config, String configName) {
        return getConfigLong(config, configName, null);
    }

    public static Long getConfigLong(AuthenticatorConfigModel config, String configName, Long defaultValue) {

        Long value = defaultValue;

        if (config.getConfig() != null) {
            // Get value
            Object obj = config.getConfig().get(configName);
            try {
                value = Long.valueOf((String) obj); // s --> ms
            } catch (NumberFormatException nfe) {
                logger.error("Can not convert " + obj + " to a number.");
            }
        }

        return value;
    }
}
