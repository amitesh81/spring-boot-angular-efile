<form:form id="caseForm" modelAttribute="caseFormModel" action="">
	<div class="step active" id="step_case">

		<div class="row stepHeader">
			<div class="col-xs-10 col-sm-10 col-md-10">
				<span> <span id="case_header_icon"></span><span
					class="stepHeaderTitle">Case</span>
				</span>
			</div>
			<div class="col-md-2" id="case_button_modify" style="display: block">
				<c:if
					test="${not empty SpringSubmissionContainer.submission.submissionNumber}">
					<span class="stepHeaderButton" id="case_header_modify"
						onclick="editCaseForm('${SpringSubmissionContainer.submission.submissionNumber}')">
						<i class="fa fa-lg fa-pencil-square-o" aria-hidden="true"></i>
					</span>
				</c:if>
			</div>
		</div>
		<div id="case_ef_step_content_display" style="display: block">
			<c:if
				test="${not empty SpringSubmissionContainer.submission.submissionNumber}">
				<div class="padding5">
					<div class="padding25left  row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<b>Case Number:&nbsp;</b>
							<c:if
								test="${not empty SpringSubmissionContainer.submission.courtCaseNumber}">
								<c:out
									value="${SpringSubmissionContainer.submission.courtCaseNumber}" />
							</c:if>
							<c:if
								test="${empty SpringSubmissionContainer.submission.courtCaseNumber}">Case Unassigned
					</c:if>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<b>Court Location:&nbsp;</b>
							<c:if
								test="${not empty SpringSubmissionContainer.submission.courtLocation}">
								<c:out
									value="${allLocationsMap[SpringSubmissionContainer.submission.courtLocation]}" />
							</c:if>
						</div>
					</div>
					<div class="padding25left row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<b>Case Category:</b>&nbsp;
							<c:if
								test="${not empty SpringSubmissionContainer.submission.caseCategory}">
								<c:out
									value="${caseCategoryMap[SpringSubmissionContainer.submission.caseCategory]}" />
							</c:if>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<b>Style of Case:</b>&nbsp;
							<c:if
								test="${not empty SpringSubmissionContainer.submission.caseType}">
								<c:out
									value="${caseTypeMap[SpringSubmissionContainer.submission.caseType]}" />
							</c:if>
						</div>
					</div>


					<div class="padding25left row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<b>Filer Reference Number:&nbsp;</b>
							<c:if
								test="${not empty SpringSubmissionContainer.submission.filerReferenceNumber}">
								<c:out
									value="${SpringSubmissionContainer.displayFilerReferenceNumber}" />
							</c:if>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<b>Filing Fee:</b>&nbsp;
							<c:if
								test="${not empty SpringSubmissionContainer.submission.filingFee}">
								<fmt:formatNumber
									value="${SpringSubmissionContainer.submission.filingFee}"
									minFractionDigits="2" maxFractionDigits="2" type="currency"
									currencySymbol="$" />
							</c:if>
						</div>
					</div>
				</div>
			</c:if>
		</div>

		<div id="case_ef_step_content_form"
			style="display: block">
			
			<br/>
			<div class="row">
				<div class="form-group">

					<div class="form-group">
						<h5>Required fields are denoted by an asterisk (*)</h5>
						<br />
						<div class="formSection">
							<div class="formTitle">Case Details</div>
							<div class="row form-group">
								<div class=" col-md-6 col-lg-6">
									<label for="courtLocation">Court Location<sup>*</sup></label>
									<form:select path="courtLocation"
										class="form-control form-control-md">
										<form:options items="${courtLocationList}" />
									</form:select>


								</div>
								<div class=" col-md-6 col-lg-6">
									<label for="caseCategory">Case Category<sup>*</sup></label>
									<form:select path="caseCategory"
										class="form-control form-control-md">
										<form:options items="${caseCategoryList}" />
									</form:select>
								</div>
								<div class=" col-md-6 col-lg-6">
									<label for="caseType">Case Type<sup>*</sup></label>
									<form:select path="caseType"
										class="form-control form-control-md">
										<option>--First select a case category--</option>
									</form:select>
								</div>
								<div class=" col-md-6 col-lg-6">
									<label for="filerReferenceNo">Filer Reference Number</label>
									<form:errors path="filerReferenceNo" cssClass="errormessage"
										element="label" />

									<form:input type="text" class="form-control form-control-md"
										name="filerReferenceNo" path="filerReferenceNo"
										aria-label="Enter Filing Location" maxlength="30" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>




			<div class="row">
			
			
			
			
			
			
			

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
				<div class="col-sm-6">
				
					<div class="form-group">
					<h5>Required fields are denoted by an asterisk (*)</h5>
						<label for="courtLocation">Court Location<sup>*</sup></label>
						<form:select path="courtLocation"
							class="form-control form-control-md">
							<form:options items="${courtLocationList}" />
						</form:select>
					</div>
					
					
					
					
					
					
					
					

					










					<div class="form-group" style="display: block"
						id="categoryContainer">
						<label for="caseCategory">Case Category<sup>*</sup></label>
						<form:select path="caseCategory"
							class="form-control form-control-md">
							<form:options items="${caseCategoryList}" />
						</form:select>
					</div>
					<div class="form-group" style="display: block" id="typeContainer">
						<label for="caseType">Case Type<sup>*</sup></label>
						<form:select path="caseType" class="form-control form-control-md">						
						<option>--First select a case category--</option>						
						</form:select>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
						<label for="filerReferenceNo">Filer Reference Number</label>
						<form:errors path="filerReferenceNo" cssClass="errormessage"
							element="label" />

						<form:input type="text" class="form-control form-control-md"
							name="filerReferenceNo" path="filerReferenceNo"
							aria-label="Enter Filing Location" maxlength="30"/>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group" id="accFeeMessage" style="display: none">
					<div class="form-group">
						<div class="alert alert-warning" role="alert">NOTE: You have
							selected an Auto Case create case type. Filing fees will be auto
							populated in the filing fee section.</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group" id="flingFeeContainer"
						style="display: none">
						<span id="feeExemptError" class="error"></span>
						<h4>For new case filings and supplemental filings that
							require a fee, please enter an amount in Filing Fee or check at
							least one box.</h4>
						<label>Filing Fee *</label>
						<form:input path="filingAmount" size="30" maxlength="8"
							class="form-control form-control-md"
							aria-label="Enter Filing Fee" value="0.00"
							style="max-width:200px;" onkeypress="return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 46"/>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
						<h4>For new case filings and supplemental filings that
							require a fee, please select any filing fee exemption that apply:</h4>

						<label class="form-check-label" for="exempt"> <form:checkbox
								class="form-check-input checkbox-lg" value="Y" path="exempt" />
							&nbsp;Exempt From Filing Fees by Section 514.040 RSMo
						</label> <br /> <label class="form-check-label" for="govAttorney">
							<form:checkbox class="form-check-input checkbox-lg" value="Y"
								path="govAttorney" /> &nbsp;Government Filer -Exempt from
							Filing Fees
						</label> <br /> <label class="form-check-label" for="informaPauperis">
							<form:checkbox class="form-check-input checkbox-lg" value="Y"
								path="informaPauperis" /> &nbsp;In Forma Pauperis
						</label> <br /> <label class="form-check-label" for="waived"> <form:checkbox
								class="form-check-input checkbox-lg" value="Y" path="waived" />
							&nbsp;Fee Waived / Not Required (explain special circumstances in
							Notes to Clerk)
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
						<h4>Enter a note to the clerk regarding processing of this
							filing (Maximum 1000 characters):</h4>
						<form:textarea path="noteToClerk" cols="75" rows="8"
							class="form-control" onkeyup="return ismaxlength('1000',this)"
							style="font-size:12px;text-transform:none;" />
					</div>
				</div>
			</div>
			<span id="btn_CaseNext" class="btn btn-primary btn-block disabled">Next</span>
		</div>
		
	</div>
</form:form>