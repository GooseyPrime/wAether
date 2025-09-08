# Environment Configuration Guide

This document explains how to configure environment variables for the wAether application.

## Setup Instructions

1. **Copy the template file:**
   ```bash
   cp .env.template .env
   ```

2. **Configure Firebase:**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Download `google-services.json` and place it in the `app/` directory
   - Enable Firestore Database in your Firebase project

3. **Review API Settings:**
   - OpenMeteo and NOAA APIs don't require keys
   - Default URLs should work for most users
   - Adjust timeout settings if needed for your network

## Environment Variables

### Firebase Configuration
- The app uses `google-services.json` for Firebase configuration
- No additional environment variables needed for Firebase

### API Configuration
| Variable | Description | Default Value |
|----------|-------------|---------------|
| `OPENMETEO_BASE_URL` | OpenMeteo weather API base URL | `https://api.open-meteo.com/v1/` |
| `NOAA_SWPC_BASE_URL` | NOAA space weather API base URL | `https://services.swpc.noaa.gov/` |

### Development Settings
| Variable | Description | Default Value |
|----------|-------------|---------------|
| `DEBUG_LOGGING_ENABLED` | Enable verbose logging | `true` |
| `MOCK_SENSOR_DATA` | Use mock sensor data for testing | `false` |

### Network Configuration
| Variable | Description | Default Value |
|----------|-------------|---------------|
| `HTTP_TIMEOUT_SECONDS` | Network request timeout | `30` |
| `RETRY_ATTEMPTS` | Number of retry attempts for failed requests | `3` |

### Background Work Configuration
| Variable | Description | Default Value |
|----------|-------------|---------------|
| `GLOBAL_SNAPSHOT_INTERVAL_HOURS` | Interval for automatic data snapshots | `1` |
| `MOOD_LOG_SYNC_INTERVAL_HOURS` | Interval for syncing mood logs | `6` |

### Wear OS Specific
| Variable | Description | Default Value |
|----------|-------------|---------------|
| `BATTERY_OPTIMIZATION_ENABLED` | Enable battery optimization features | `true` |
| `REDUCE_ANIMATIONS` | Reduce animations for performance | `false` |

### Testing Configuration
| Variable | Description | Default Value |
|----------|-------------|---------------|
| `ENABLE_TEST_MODE` | Enable test mode features | `false` |
| `MOCK_LOCATION_DATA` | Use mock location data | `false` |

## Security Considerations

- **Never commit `.env` files** to version control
- Keep your `google-services.json` file secure
- Use environment-specific configurations for different deployments
- Regularly review and rotate any API keys (when applicable)

## Troubleshooting

### Firebase Issues
- Ensure `google-services.json` is in the correct location (`app/` directory)
- Verify Firestore is enabled in your Firebase project
- Check Firebase project permissions and authentication settings

### API Issues
- Verify internet connectivity on your Wear OS device
- Check API URLs are accessible
- Adjust timeout settings if experiencing network issues

### Build Issues
- Ensure environment variables are properly formatted
- Check for syntax errors in the `.env` file
- Verify all required dependencies are installed

## Development vs Production

For different environments, you may want different configurations:

### Development
```bash
DEBUG_LOGGING_ENABLED=true
MOCK_SENSOR_DATA=true
ENABLE_TEST_MODE=true
```

### Production
```bash
DEBUG_LOGGING_ENABLED=false
MOCK_SENSOR_DATA=false
ENABLE_TEST_MODE=false
BATTERY_OPTIMIZATION_ENABLED=true
```