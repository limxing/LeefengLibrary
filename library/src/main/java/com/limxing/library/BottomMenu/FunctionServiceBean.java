/*Copy from TommyLemon(https://github.com/TommyLemon)

*/

package com.limxing.library.BottomMenu;

import android.content.Intent;

public class FunctionServiceBean {

	private int bolongGroup;
	private String name;
	private int imageRes;
	private int operationType;
	private Intent operationIntent;
	private int intentCode;
	
	public FunctionServiceBean() {
		// TODO Auto-generated constructor stub
	}
	public FunctionServiceBean(String name) {
		this.name = name;
	}
	public FunctionServiceBean(int bolongGroup, String name) {
		this.bolongGroup = bolongGroup;
		this.name = name;
	}
	public FunctionServiceBean(String name, int imageRes) {
		this.name = name;
		this.imageRes = imageRes;
	}
	public FunctionServiceBean(String name, int imageRes, int intentCode) {
		this.name = name;
		this.imageRes = imageRes;
		this.intentCode = intentCode;
	}
	public FunctionServiceBean(int bolongGroup, String name, int operationType, Intent operationIntent) {
		this.bolongGroup = bolongGroup;
		this.name = name;
		this.operationType = operationType;
		this.operationIntent = operationIntent;
	}
	public FunctionServiceBean(int bolongGroup, String name, int operationType, Intent operationIntent, int intentCode) {
		this.bolongGroup = bolongGroup;
		this.name = name;
		this.operationType = operationType;
		this.operationIntent = operationIntent;
		this.intentCode = intentCode;
	}
	public FunctionServiceBean(int bolongGroup, String name, int imageRes, int operationType, Intent operationIntent) {
		this.bolongGroup = bolongGroup;
		this.name = name;
		this.imageRes = imageRes;
		this.operationType = operationType;
		this.operationIntent = operationIntent;
	}
	public FunctionServiceBean(int bolongGroup, String name, int imageRes, int operationType, Intent operationIntent, int intentCode) {
		this.bolongGroup = bolongGroup;
		this.name = name;
		this.imageRes = imageRes;
		this.operationType = operationType;
		this.operationIntent = operationIntent;
		this.intentCode = intentCode;
	}

	
	public int getIntentCode() {
		return intentCode;
	}

	public void setIntentCode(int intentCode) {
		this.intentCode = intentCode;
	}

	public int getImageRes() {
		return imageRes;
	}

	public void setImageRes(int imageRes) {
		this.imageRes = imageRes;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBolongGroup() {
		return bolongGroup;
	}
	public void setBolongGroup(int bolongGroup) {
		this.bolongGroup = bolongGroup;
	}
	public int getOperationType() {
		return operationType;
	}
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}
	public Intent getOperationIntent() {
		return operationIntent;
	}
	public void setOperationIntent(Intent operationIntent) {
		this.operationIntent = operationIntent;
	}
	
}
