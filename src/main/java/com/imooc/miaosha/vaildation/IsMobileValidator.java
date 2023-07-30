package com.imooc.miaosha.vaildation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.engine.validationcontext.ReturnValueExecutableValidationContext;

import com.alibaba.druid.util.StringUtils;
import com.imooc.miaosha.util.ValidatorUtil;


public class IsMobileValidator implements ConstraintValidator<IsMobile,String>{
	private boolean required =false;
	@Override
	public void initialize(IsMobile constraintAnnotation) {
		// TODO 自动生成的方法存根
		required=constraintAnnotation.required();
	}
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			return ValidatorUtil.isMobile(value);
		}else {
			if(StringUtils.isEmpty(value)) {
				return true;
			}else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}

}
