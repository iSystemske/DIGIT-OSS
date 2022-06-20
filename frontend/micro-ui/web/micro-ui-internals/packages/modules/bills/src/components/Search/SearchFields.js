import React, { Fragment } from "react";
import { Controller, useWatch } from "react-hook-form";
import {
  TextInput,
  SubmitBar,
  LinkLabel,
  ActionBar,
  CloseSvg,
  DatePicker,
  CardLabelError,
  SearchForm,
  SearchField,
  Dropdown,
  Table,
  Card,
  MobileNumber,
  Loader,
  CardText,
  Header,
} from "@egovernments/digit-ui-react-components";

const SearchFields = ({ register, control, reset, tenantId, t, previousPage ,formState}) => {


  const { isLoading, data: generateServiceType } = Digit.Hooks.useCommonMDMS(tenantId, "BillingService", "BillsGenieKey");
  
    const filterServiceType = generateServiceType?.BillingService?.BusinessService?.filter((element) => element.billGineiURL);
  
    const getUlbLists = generateServiceType?.tenant?.tenants?.filter((element) => element.code === tenantId);
    let serviceTypeList = [];
    if (filterServiceType) {
      serviceTypeList = filterServiceType.map((element) => {
        return {
          name: Digit.Utils.locale.getTransformedLocale(`BILLINGSERVICE_BUSINESSSERVICE_${element.code}`),
          url: element.billGineiURL,
          businesService: element.code,
        };
      });
    }

    const formErrors = formState?.errors
    const propsForMobileNumber = {
        maxlength: 10,
        pattern: "[6-9][0-9]{9}",
        title: t("ES_SEARCH_APPLICATION_MOBILE_INVALID"),
        componentInFront: "+91",
    };
  return (
    <>
      < div style={{marginRight:"542px", marginBottom:"10px"}}>
                <span style={{color:"#505A5F"}}>{t("Provide at least one parameter to search for an application")}</span>
                </div>
      {isLoading ? (
        <Loader />
      ) : (  
        <SearchField>
           <label>{t("ABG_SERVICE_CATEGORY_LABEL")}</label>
          <Controller
            control={control}
            rules={{ required: t("REQUIRED_FIELD") }}
            name="serviceCategory"
            render={(props) => (
              <Dropdown name="serviceCategory" t={t} option={serviceTypeList} onBlur={props.onBlur} selected={props.value}  select={props.onChange} optionKey={"name"} />
            )}
          />
           {formErrors && formErrors?.businesService && formErrors?.businesService?.type === "required" && (
                    <CardLabelError>{t(`CS_COMMON_REQUIRED`)}</CardLabelError>)}
        </SearchField>
      )}
      <SearchField>
        <label>{t("ABG_BILL_NUMBER_LABEL")}</label>
        <TextInput name="billNumber" inputRef={register({})} />
      </SearchField>
      <SearchField>
        <label>{t("ABG_PROPERTYID_CONS_NO")}</label>
        <TextInput name="consumerCode" inputRef={register({})} />
      </SearchField>
      <SearchField>
                <label>{t("ABG_MOBILE_NO_LABEL")}</label>
                <MobileNumber name="mobileNumber" inputRef={register({})} {...propsForMobileNumber} />
            </SearchField>

      <SearchField className="submit">
        <SubmitBar label={t("ES_COMMON_SEARCH")} submit />
        <p 
          onClick={() => {
            reset({
              serviceCategory: "",
              consumerCode: "",
              billNumber: "",
              mobileNumber: "",
              offset: 0,
              limit: 10,
              sortBy: "commencementDate",
              sortOrder: "DESC",
            });
            previousPage();
          }}
        >
          {t(`ES_COMMON_CLEAR_ALL`)}
        </p>
      </SearchField>
    </>
  );
};
export default SearchFields;
