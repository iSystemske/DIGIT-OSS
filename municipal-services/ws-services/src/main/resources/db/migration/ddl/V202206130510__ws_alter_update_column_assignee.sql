UPDATE eg_ws_connection conn 
SET assignee=(SELECT cte.assignee 
FROM (SELECT assg.assignee,pi.businessid
FROM eg_wf_processinstance_v2 pi 
INNER JOIN eg_wf_assignee_v2 assg ON pi.id = assg.processinstanceid) cte 
WHERE cte.businessid = conn.applicationno);