#!/bin/bash
# Quick Setup Script for Email Invitation Feature

echo "=========================================="
echo "Event Chey API - Email Setup"
echo "=========================================="
echo ""

# Check if .env file exists
if [ -f .env ]; then
    echo "Found existing .env file. Loading..."
    source .env
else
    echo "Creating new .env file..."
    touch .env
fi

# Function to prompt for input
prompt_config() {
    local var_name=$1
    local prompt_text=$2
    local default_value=$3
    local current_value=${!var_name}

    if [ -z "$current_value" ]; then
        read -p "$prompt_text [$default_value]: " input_value
        input_value=${input_value:-$default_value}
        echo "export $var_name=\"$input_value\"" >> .env
    else
        echo "  $var_name is already set: $current_value"
    fi
}

echo "Configuring email settings..."
echo ""

# Prompt for configuration
prompt_config "EMAIL_USERNAME" "Enter email username (e.g., your-email@gmail.com)" ""
prompt_config "EMAIL_PASSWORD" "Enter email password or app password" ""
prompt_config "EMAIL_FROM" "Enter 'from' email address" "noreply@eventchey.com"
prompt_config "EMAIL_ENABLED" "Enable email sending? (true/false)" "false"

echo ""
echo "=========================================="
echo "Configuration saved to .env file"
echo "=========================================="
echo ""
echo "To use these settings, run:"
echo "  source .env"
echo "  ./mvnw spring-boot:run"
echo ""
echo "Or export them directly:"
cat .env
echo ""
echo "=========================================="
echo "Gmail Setup Instructions:"
echo "=========================================="
echo "1. Go to https://myaccount.google.com/"
echo "2. Enable 2-factor authentication"
echo "3. Generate an App Password at:"
echo "   https://myaccount.google.com/apppasswords"
echo "4. Use the App Password as EMAIL_PASSWORD"
echo ""
echo "=========================================="
echo "Testing without sending real emails:"
echo "=========================================="
echo "Set EMAIL_ENABLED=false to test without"
echo "sending actual emails. Check logs to see"
echo "what would have been sent."
echo ""
echo "For more information, see:"
echo "  - EMAIL_FEATURE.md"
echo "  - EMAIL_EXAMPLES.md"
echo "  - EMAIL_IMPLEMENTATION_SUMMARY.md"
echo ""

