#!/bin/bash

# Define the google-services.json content as a JSON object using environment variables
cat <<EOF > app/google-services.json
{
  "project_info": {
    "project_number": "${FIREBASE_PROJECT_NUMBER}",
    "project_id": "${FIREBASE_PROJECT_ID}",
    "storage_bucket": "${FIREBASE_STORAGE_BUCKET}"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "${FIREBASE_MOBILESDK_APP_ID}",
        "android_client_info": {
          "package_name": "${FIREBASE_CLIENT_PACKAGE_NAME}"
        }
      },
      "api_key": [
        {
          "current_key": "${FIREBASE_API_KEY}"
        }
      ],
      "services": {
        "appinvite_service": {
          "status": 2,
          "other_platform_oauth_client": []
        }
      }
    }
  ],
  "configuration_version": "1"
}
EOF