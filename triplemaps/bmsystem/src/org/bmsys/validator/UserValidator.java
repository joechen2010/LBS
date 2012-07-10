package org.bmsys.validator;

import org.bmsys.form.UserCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Component("userValidator")
public class UserValidator
{
	/**
	 * 
	 * @param user
	 * @param result
	 */
	public void validateUser(UserCommand user, BindingResult result)
	{
		if (user.getFirstName() == null
				|| user.getFirstName().trim().length() > 50)
		{
			result.addError(new ObjectError("error", "test error"));
		}
		else if (user.getMiddleName() == null
				|| user.getMiddleName().trim().length() > 50)
		{
			result.addError(new ObjectError("", ""));
		}
		else if (user.getLastName() == null
				|| user.getLastName().trim().length() > 50)
		{

		}
		else if (user.getAddress1() == null
				|| user.getAddress1().trim().length() > 50)
		{
			result.addError(new ObjectError("", ""));
		}
		else if (user.getContactNo() == null
				|| user.getContactNo().trim().length() > 50)
		{
			result.addError(new ObjectError("", ""));
		}
		else if (user.getReccuringAmount() == null)
		{
			result.addError(new ObjectError("", ""));
		}
	}
}