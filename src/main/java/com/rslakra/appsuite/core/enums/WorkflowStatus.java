package com.rslakra.appsuite.core.enums;

/**
 * A common set of statuses for fixing bugs are those shown above. “On Hold” is the first state of a bug after it gets
 * reported. “In Progress” indicates that the bug is being fixed, where the bug to be fixed is chosen based on level of
 * severity and then gets assigned to the team/person responsible for fixing it. Once the bug has been resolved, it
 * would have a “Fixed” status, which typically acts as the Quality Assurance team backlog. “Under Review” means it is
 * being processed by QA, while “Approved” means the bug fix has passed review. “Deployed” means that the bug fix is
 * live in production. Keep in mind that an issue can pass through different statuses multiple times until it reaches
 * its final status, which is “Closed”.
 *
 * @author Rohtash Lakra
 * @created 9/20/22 3:21 PM
 */
public enum WorkflowStatus {
    APPROVED,
    BACKLOG,
    CLOSED,
    DEPLOYED,
    FIXED,
    IN_PROGRESS,
    ON_HOLD,
    RELEASED,
    UNDER_REVIEW;
}
