create table member (member_id bigint not null auto_increment, username varchar(255) not null, nickname varchar(20) not null, user_status_message varchar(100), profile_img_url varchar(255), primary key (member_id));
create table post (post_id bigint not null auto_increment, member_id bigint not null, title varchar(100), content varchar(255), main_image_id bigint, consecutive_days integer not null, created_date datetime(6), primary key (post_id));
create table comment (comment_id bigint not null auto_increment, member_id bigint not null, post_id bigint not null, parent_comment_id bigint, content varchar(255), created_date datetime(6), primary key (comment_id));
create table post_image (post_image_id bigint not null auto_increment, post_id bigint not null, img_url varchar(255), primary key (post_image_id));
create table member_post_liked (member_id bigint not null, post_id bigint not null, primary key (member_id, post_id));
create table goal (goal_id bigint not null auto_increment, member_id bigint not null, content varchar(255), is_achieved tinyint not null, primary key (goal_id));

alter table comment add constraint fk_comment_member foreign key (member_id) references member (member_id) on delete cascade;
alter table comment add constraint fk_comment_comment foreign key (parent_comment_id) references comment (comment_id) on delete cascade;
alter table comment add constraint fk_comment_post foreign key (post_id) references post (post_id) on delete cascade;
alter table goal add constraint fk_goal_member foreign key (member_id) references member (member_id) on delete cascade;
alter table member_post_liked add constraint fk_liked_member foreign key (member_id) references member (member_id) on delete cascade;
alter table member_post_liked add constraint fk_liked_post foreign key (post_id) references post (post_id) on delete cascade;
alter table post add constraint fk_post_main_image foreign key (main_image_id) references post_image (post_image_id) on delete set null;
alter table post add constraint fk_post_member foreign key (member_id) references member (member_id) on delete cascade;
alter table post_image add constraint fk_image_post foreign key (post_id) references post (post_id) on delete cascade;