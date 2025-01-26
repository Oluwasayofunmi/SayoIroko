# Calculate the financial projections based on user input.
# We will create two scenarios: one for 50 gyms and one for 500 gyms.

# Assumptions:
# 1. Revenue per installed unit = €2,500
# 2. Maintenance contracts per gym per month = €100
# 3. Recurring maintenance revenue per year = 100 * 12
# 4. Cost savings per gym per year = €5,000
# 5. Projection period = 1 year

# Scenario 1: 50 gyms
# Scenario 2: 500 gyms

# Constants
unit_price = 2500  # price per unit
maintenance_per_month = 100  # monthly maintenance fee per gym
savings_per_gym_per_year = 5000  # energy savings per gym per year

# For 50 gyms
gyms_50 = 50
revenue_from_units_50 = gyms_50 * unit_price
maintenance_revenue_50 = gyms_50 * maintenance_per_month * 12
total_savings_50 = gyms_50 * savings_per_gym_per_year

# For 500 gyms
gyms_500 = 500
revenue_from_units_500 = gyms_500 * unit_price
maintenance_revenue_500 = gyms_500 * maintenance_per_month * 12
total_savings_500 = gyms_500 * savings_per_gym_per_year

# Compile the results into a dictionary for easier display
financial_projections = {
    "50 Gyms": {
        "Revenue from Unit Sales (€)": revenue_from_units_50,
        "Recurring Maintenance Revenue (€)": maintenance_revenue_50,
        "Total Energy Savings for Gyms (€)": total_savings_50,
    },
    "500 Gyms": {
        "Revenue from Unit Sales (€)": revenue_from_units_500,
        "Recurring Maintenance Revenue (€)": maintenance_revenue_500,
        "Total Energy Savings for Gyms (€)": total_savings_500,
    }
}

import pandas as pd
financial_df = pd.DataFrame(financial_projections)
import ace_tools as tools; tools.display_dataframe_to_user(name="Financial Projections", dataframe=financial_df)
