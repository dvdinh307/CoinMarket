package midas.coinmarket.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import midas.coinmarket.R;

public class AccountUtils {

    public static Account getAccountUser(Activity activity, AccountManager manager) {
        Account[] accounts = manager.getAccounts();
        Account accountResult = null;
        for (Account account : accounts) {
            String name = account.name;
            String type = account.type;
            if (type.equalsIgnoreCase(activity.getString(R.string.account_type))) {
                Log.e("Account name", "Values :" + name);
                accountResult = account;
            }
        }
        return accountResult;
    }

    public static void saveAccountInformation(Activity activity, String email, String password) {
        AccountManager mManagerAccount = AccountManager.get(activity);
        String accountType = activity.getString(R.string.account_type);
        // This is the magic that addes the account to the Android Account Manager
        final Account account = new Account(email, accountType);
        mManagerAccount.addAccountExplicitly(account, password, null);
        // Now we tell our caller, could be the Android Account Manager or even our own application
        // that the process was successful
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, accountType);
        activity.setResult(Activity.RESULT_OK, intent);
    }

    public static void clearAllAccountOfThisApplication(Activity activity) {
        AccountManager mManagerAccount = AccountManager.get(activity);
        String accountType = activity.getString(R.string.account_type);
        Account[] accounts = mManagerAccount.getAccounts();
        if (accounts.length > 0) {
            for (int index = 0; index < accounts.length; index++) {
                if (accounts[index].type.intern().equalsIgnoreCase(accountType))
                    mManagerAccount.removeAccount(accounts[index], new AccountManagerCallback<Boolean>() {
                        @Override
                        public void run(AccountManagerFuture<Boolean> accountManagerFuture) {
                            Log.e("Values", "------" + accountManagerFuture);
                        }
                    }, null);
            }
        }
    }

}
