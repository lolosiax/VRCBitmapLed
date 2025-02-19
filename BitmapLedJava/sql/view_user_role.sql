DROP VIEW IF EXISTS view_user_role;

CREATE VIEW view_user_role AS
SELECT sys_user_roles.id         AS id,
       sys_user_roles.user_id    AS user_id,
       sys_user.user_name        AS user_name,
       sys_user.real_name        AS real_name,
       sys_user.phone            AS phone,
       sys_user.email            AS email,
       sys_user.avatar           AS avatar,
       sys_role.id               AS role_id,
       sys_role.role_name        AS role_name,
       sys_role.type             AS type,
       sys_user_roles.created_at AS created_at,
       sys_user_roles.updated_at AS updated_at,
       sys_user_roles.created_by AS created_by,
       sys_user_roles.updated_by AS updated_by,
       sys_user_roles.deleted    AS deleted
FROM (sys_user_roles
    JOIN sys_role ON ((sys_user_roles.role_id = sys_role.id) AND (sys_role.deleted != 1))
    JOIN sys_user ON (sys_user_roles.user_id = sys_user.id and sys_user.deleted != 1)
         );