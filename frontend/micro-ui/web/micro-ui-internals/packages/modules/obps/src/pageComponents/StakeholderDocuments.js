import React, { useEffect, useState } from "react";
import {
    CardLabel,
    Dropdown,
    UploadFile,
    Toast,
    Loader,
    FormStep,
    CitizenInfoLabel,
    OpenLinkContainer,
    BackButton 
} from "@egovernments/digit-ui-react-components";
import Timeline from "../components/Timeline";

const StakeholderDocuments = ({ t, config, onSelect, userType, formData, setError: setFormError, clearErrors: clearFormErrors, formState }) => {
    const tenantId = Digit.ULBService.getCurrentTenantId();
    const stateId = Digit.ULBService.getStateId();
    const [documents, setDocuments] = useState(formData?.documents?.documents || []);
    const [error, setError] = useState(null);
    const [bpaTaxDocuments, setBpaTaxDocuments] = useState([]);
    const [enableSubmit, setEnableSubmit] = useState(true)
    const [checkRequiredFields, setCheckRequiredFields] = useState(false);
    const isCitizenUrl = Digit.Utils.browser.isMobile()?true:false;
    let isopenlink = window.location.href.includes("/openlink/");

    const { data, isLoading } = Digit.Hooks.obps.useMDMS(stateId, "StakeholderRegistraition", "TradeTypetoRoleMapping");
    

    useEffect(() => {
        let filtredBpaDocs = [];
        if (data?.StakeholderRegistraition?.TradeTypetoRoleMapping) {
            //filtredBpaDocs = bpaDocs?.BPA?.DocTypeMapping?.filter(data => (data.WFState == "INITIATED" && data.RiskType == "LOW" && data.ServiceType == "NEW_CONSTRUCTION" && data.applicationType == "BUILDING_PLAN_SCRUTINY"))
        //    let  formData = {formdata:{LicenseType:{LicenseType:{tradeType : "ENGINEER.CLASSA",}}}}
            filtredBpaDocs = data?.StakeholderRegistraition?.TradeTypetoRoleMapping?.filter(ob => (ob.tradeType === formData?.formData?.LicneseType?.LicenseType?.tradeType))
        }

        let documentsList = [];
        filtredBpaDocs?.[0]?.docTypes?.forEach(doc => {
            let code = doc.code; doc.dropdownData = [];
            // commonDocs?.["common-masters"]?.DocumentType?.forEach(value => {
            //     let values = value.code.slice(0, code.length);
            //     if (code === values) {
            //         doc.hasDropdown = true;
            //         value.i18nKey = value.code;
            //         doc.dropdownData.push(value);
            //     }
            // });
            documentsList.push(doc);
        });
        setBpaTaxDocuments(documentsList);

    }, [!isLoading]);

    const handleSubmit = () => {
        let document = formData.documents;
        let documentStep;
        documentStep = { ...document, documents: documents };
        onSelect(config.key, documentStep);
     };
    const onSkip = () => onSelect();
    function onAdd() { }

    useEffect(() => {
        let count = 0;
        bpaTaxDocuments.map(doc => {
            let isRequired = false;
            documents.map(data => {
                if (doc.required && doc.code == `${data.documentType.split('.')[0]}.${data.documentType.split('.')[1]}`) {
                    isRequired = true;
                }
            });
            if (!isRequired && doc.required) {
                count = count + 1;
            }
        });
        //if(bpaTaxDocuments.length == documents.length+1 && bpaTaxDocuments.length!==0) setEnableSubmit(false);
        if ((count == "0" || count == 0) && documents.length > 0) setEnableSubmit(false);
        else setEnableSubmit(true);
    }, [documents, checkRequiredFields])

    // if (bpaDocsLoading) {
    //     return <Loader />;
    // }

    return (
        <div>
            <div className={isopenlink? "OpenlinkContainer":""}>
            {isopenlink &&<OpenLinkContainer />}
            <div style={isopenlink?{marginTop:"60px", width:isCitizenUrl?"100%":"70%", marginLeft:"auto",marginRight:"auto"}:{}}>
            {isopenlink && <BackButton style={{ border: "none" }}>{t("CS_COMMON_BACK")}</BackButton>}
            <Timeline currentStep={3} flow="STAKEHOLDER" />
            {!isLoading ?
                <FormStep
                    t={t}
                    config={config}
                    onSelect={handleSubmit}
                    onSkip={onSkip}
                    isDisabled={enableSubmit}
                    onAdd={onAdd}
                >
                    {bpaTaxDocuments?.map((document, index) => {
                        return (
                            <SelectDocument
                                key={index}
                                document={document}
                                t={t}
                                error={error}
                                setError={setError}
                                setDocuments={setDocuments}
                                documents={documents}
                                setCheckRequiredFields={setCheckRequiredFields}
                                isCitizenUrl={isCitizenUrl}
                            />
                        );
                    })}
                    {error && <Toast label={error} isDleteBtn={true} onClose={() => setError(null)} error  />}
                </FormStep> : <Loader />}
                <CitizenInfoLabel info={t("CS_FILE_APPLICATION_INFO_LABEL")} text={`${t("BPA_APPLICATION_NUMBER_LABEL")} ${formData?.result?.Licenses?.[0]?.applicationNumber} ${t("BPA_DOCS_INFORMATION")}`} />
                </div>
            </div>
        </div>
    );
}

function SelectDocument({
    t,
    document: doc,
    setDocuments,
    error,
    setError,
    documents,
    setCheckRequiredFields,
    isCitizenUrl
}) {

    const filteredDocument = documents?.filter((item) => item?.documentType?.includes(doc?.code))[0];
    const tenantId = Digit.ULBService.getCurrentTenantId();
    const [selectedDocument, setSelectedDocument] = useState(
        filteredDocument
            ? { ...filteredDocument, active: true, code: filteredDocument?.documentType, i18nKey: filteredDocument?.documentType }
            : doc?.dropdownData?.length === 1
                ? doc?.dropdownData[0]
                : {}
    );
    const [file, setFile] = useState(null);
    const [uploadedFile, setUploadedFile] = useState(() => filteredDocument?.fileStoreId || null);

    const handleSelectDocument = (value) => setSelectedDocument(value);

    function selectfile(e) {
        setFile(e.target.files[0]);
    }

    useEffect(() => {

        setDocuments((prev) => {
            const filteredDocumentsByDocumentType = prev?.filter((item) => item?.documentType !== doc?.code);

            if (uploadedFile?.length === 0 || uploadedFile === null) {
                return filteredDocumentsByDocumentType;
            }

            const filteredDocumentsByFileStoreId = filteredDocumentsByDocumentType?.filter((item) => item?.fileStoreId !== uploadedFile);
            return [
                ...filteredDocumentsByFileStoreId,
                {
                    documentType: doc?.code,
                    fileStoreId: uploadedFile,
                    documentUid: uploadedFile,
                    fileName: file?.name || "",
                },
            ];
        });       
    }, [uploadedFile,file]);


    useEffect(() => {
        (async () => {
            setError(null);
            if (file) {
                if (file.size >= 5242880) {
                    setError(t("CS_MAXIMUM_UPLOAD_SIZE_EXCEEDED"));
                } else {
                    try {
                        setUploadedFile(null);
                        const response = await Digit.UploadServices.Filestorage("PT", file, tenantId?.split(".")[0]);
                        if (response?.data?.files?.length > 0) {
                            setUploadedFile(response?.data?.files[0]?.fileStoreId);
                        } else {
                            setError(t("CS_FILE_UPLOAD_ERROR"));
                        }
                    } catch (err) {
                        console.error("Modal -> err ", err);
                        setError(t("CS_FILE_UPLOAD_ERROR"));
                    }
                }
            }
        })();
    }, [file]);

    return (
        <div style={{ marginBottom: "24px" }}>
            <CardLabel>{doc?.required ? `${t(doc?.code)} *` : `${t(doc?.code)}`}</CardLabel>
            {/* <Dropdown
                t={t}
                isMandatory={false}
                option={doc?.dropdownData}
                selected={selectedDocument}
                optionKey="i18nKey"
                select={handleSelectDocument}
            /> */}
            <UploadFile
                extraStyleName={"propertyCreate"}
                accept=".jpg,.png,.pdf"
                onUpload={selectfile}
                onDelete={() => {
                    setUploadedFile(null);
                    setCheckRequiredFields(true);
                }}
                message={uploadedFile ? `1 ${t(`CS_ACTION_FILEUPLOADED`)}` : t(`CS_ACTION_NO_FILEUPLOADED`)}
                error={error}
                Multistyle={isCitizenUrl?{marginTop:"-15px",position:"absolute"}:{marginTop:"-15px",position:"absolute"}}
            />
        </div>
    );

}

export default StakeholderDocuments;
