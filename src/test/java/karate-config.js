function karateConfig() {
    var env = karate.env; // get system property 'karate.env'
    karate.log('karate.env system property was:', env);
    if (!env) {
        env = 'dev';
    }
    var config = {
        env: env,
        baseUrl: 'http://localhost:8080/'
    }

    if (env == 'dev') {
        config.baseUrl = 'http://localhost:8080/';
    }

    else if (env == 'qa') {
        config.baseUrl = 'http://localhost:8080/'; // TODO Use correct URL for QA with domain name and https
    }

    else if (env == 'prod') {
        config.baseUrl = 'http://localhost:8080/'; // TODO Use correct URL for PROD with domain name and https
    }

    return config;
}