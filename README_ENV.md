# wAether Environment Variables Guide

This document describes all environment variables used by the wAether Android Wear OS application and provides guidance for configuration in different environments.

## Quick Start

1. Copy the template file:
   ```bash
   cp .env.template .env
   ```

2. Edit `.env` with your actual configuration values
3. Ensure `.env` is in your `.gitignore` (it should be already)

## Environment Variables Reference

### Firebase Configuration

The wAether app uses Firebase Firestore for data persistence. These values are obtained from your Firebase Console.

| Variable | Required | Description | Example |
|----------|----------|-------------|---------|
| `FIREBASE_PROJECT_ID` | Yes | Your Firebase project ID | `waether-app-prod` |
| `FIREBASE_API_KEY` | Yes | Firebase Web API key | `AIzaSyC...` |
| `FIREBASE_APP_ID` | Yes | Firebase App ID | `1:123456789:android:abc123` |
| `FIREBASE_MESSAGING_SENDER_ID` | Yes | Firebase Cloud Messaging sender ID | `123456789` |

**Setup Instructions:**
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select existing
3. Add an Android app to your project
4. Download `google-services.json` and place in `app/` directory
5. Copy configuration values to your `.env` file

### External API Configuration

#### OpenMeteo Weather API

| Variable | Required | Description | Default |
|----------|----------|-------------|---------|
| `OPENMETEO_API_KEY` | No | API key for higher rate limits | Not required for basic usage |

- **Free Tier**: 10,000 requests/day, no key required
- **Paid Tiers**: Higher limits available at [openmeteo.com](https://openmeteo.com/)

#### NOAA Space Weather API

| Variable | Required | Description | Default |
|----------|----------|-------------|---------|
| `NOAA_SWPC_BASE_URL` | No | Base URL for NOAA API | `https://services.swpc.noaa.gov` |

- **Free**: No API key required
- **Documentation**: [SWPC Products](https://www.swpc.noaa.gov/products)

### Development Configuration

| Variable | Description | Development | Production |
|----------|-------------|-------------|------------|
| `DEBUG_LOGGING` | Enable debug logs | `true` | `false` |
| `LOG_LEVEL` | Logging level | `DEBUG` | `WARN` |
| `NETWORK_LOGGING` | Log HTTP requests | `true` | `false` |
| `ENVIRONMENT` | Environment name | `development` | `production` |

### Performance and Monitoring

| Variable | Description | Default |
|----------|-------------|---------|
| `DATA_REFRESH_INTERVAL_MS` | How often to fetch data (ms) | `300000` (5 minutes) |
| `GLOBAL_SNAPSHOT_INTERVAL_HOURS` | Background snapshot frequency | `6` |
| `ENABLE_BACKGROUND_WORKERS` | Enable WorkManager workers | `true` |
| `CRASH_REPORTING_ENABLED` | Enable crash reporting | `false` |
| `PERFORMANCE_MONITORING_ENABLED` | Enable performance monitoring | `false` |

## Environment-Specific Configurations

### Development Environment

```bash
ENVIRONMENT=development
DEBUG_LOGGING=true
LOG_LEVEL=DEBUG
NETWORK_LOGGING=true
CRASH_REPORTING_ENABLED=false
PERFORMANCE_MONITORING_ENABLED=false
```

### Staging Environment

```bash
ENVIRONMENT=staging
DEBUG_LOGGING=true
LOG_LEVEL=INFO
NETWORK_LOGGING=false
CRASH_REPORTING_ENABLED=true
PERFORMANCE_MONITORING_ENABLED=true
```

### Production Environment

```bash
ENVIRONMENT=production
DEBUG_LOGGING=false
LOG_LEVEL=WARN
NETWORK_LOGGING=false
CRASH_REPORTING_ENABLED=true
PERFORMANCE_MONITORING_ENABLED=true
```

## Security Best Practices

1. **Never commit `.env` files** - They contain sensitive configuration
2. **Use different Firebase projects** for dev/staging/production
3. **Rotate API keys regularly** if using paid services
4. **Monitor API usage** to detect unauthorized access
5. **Use environment-specific configurations** to limit debug data in production

## Validation

To validate your environment configuration:

1. **Build verification**:
   ```bash
   ./gradlew assembleDebug
   ```

2. **Firebase connection test**:
   - Deploy to a test device
   - Check logs for Firebase initialization success
   - Verify data can be written to Firestore

3. **API connectivity test**:
   - Check network logs for successful API calls
   - Verify weather and space weather data is being fetched

## Troubleshooting

### Common Issues

**Firebase connection fails:**
- Verify `google-services.json` is in the correct location
- Check Firebase project settings match your configuration
- Ensure Firebase Authentication is properly configured

**API calls failing:**
- Check network connectivity
- Verify API endpoints are accessible
- Review rate limiting (especially for OpenMeteo)

**Performance issues:**
- Adjust `DATA_REFRESH_INTERVAL_MS` for your use case
- Monitor background worker frequency
- Check device memory and CPU usage

## Environment File Security

- Store production environment files securely (encrypted)
- Use secret management systems in CI/CD pipelines
- Regularly audit access to environment configurations
- Document who has access to production environment variables

---

**Last Updated**: 2025-09-08T13:00:00-04:00 / 2025-09-08T17:00:00Z — Initial environment configuration documentation