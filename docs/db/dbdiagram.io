Enum partner_status {
  ACTIVE
  INACTIVE
}

Enum room_status {
  AVAILABLE
  BOOKED
  MAINTENANCE
}

Enum payment_method {
  CREDIT_CARD
  DEBIT_CARD
  PAYPAL
  BANK_TRANSFER
  PIX
  CASH
}

Enum payment_status {
  PENDING
  COMPLETED
  REFUNDED
  FAILED
}

Enum booking_status {
  CONFIRMED
  CANCELLED
  IN_PROGRESS
  COMPLETED
}

Table partner {
  id uuid [pk]
  name varchar(255) [not null]
  cnpj varchar(255) [not null]
  email varchar(255) [unique, not null]
  phone varchar(20) [not null]
  address varchar(255) [not null]
  contact_name varchar(255)
  contact_email varchar(255)
  contact_phone varchar(20)
  contract_signed boolean [not null, default: false]
  status partner_status [default: "ACTIVE"]
  created_at datetime [default: `now()`]
  notes text
}

Table hotel {
  id uuid [pk]
  partner_id uuid [not null, ref: > partner.id]
  name varchar(255) [not null]
  address varchar(255) [not null]
  city varchar(100) [not null]
  state varchar(50) [not null]
  zip_code varchar(20) [not null]
  phone varchar(20) [not null]
  rating decimal(1,1) [not null, default: 0.0]
  description text
  website varchar(255)
  latitude decimal(10,6) [not null]
  longitude decimal(10,6) [not null]
}

Table room {
  id uuid [pk]
  hotel_id uuid [not null, ref: > hotel.id]
  number varchar(10) [not null]
  floor int [not null]
  type varchar(50) [not null]
  price decimal(10,2)
  capacity int [not null]
  status room_status [not null, default: "AVAILABLE"]
  description text
}

Table customer {
  id uuid [pk]
  name varchar(255) [not null]
  email varchar(255) [unique, not null]
  phone varchar(20) [not null]
  id_document varchar(50) [not null]
  birth_date date [not null]
  address varchar(255) [not null]
}


Table booking {
  id uuid [pk]
  customer_id uuid [not null, ref: > customer.id]
  room_id uuid [not null, ref: > room.id]
  booked_at datetime [not null, default: `now()`]
  checkin datetime [not null]
  checkout datetime [not null]
  guests int [not null]
  status booking_status [not null, default: "CONFIRMED"]
  notes text
}

Table payment {
  id uuid [pk]
  booking_id uuid [not null, ref: > booking.id]
  transaction_id varchar(100) [unique, not null]
  amount decimal(10,2) [not null]
  payment_method payment_method [not null]
  status payment_status [not null, default: "PENDING"]
  paid_at datetime [not null, default: `now()`]
}

Table review {
  id uuid [pk]
  booking_id uuid [not null, ref: > booking.id]
  rating int [not null, note: "rating >= 1 and rating <= 5"]
  comment text
  reviewed_at datetime [not null, default: `now()`]
}

Table customer_auth {
  id uuid [pk]
  customer_id uuid [not null, unique, ref: > customer.id]
  email varchar(255) [not null, unique]
  password_hash varchar(255) [not null]
  created_at datetime [not null, default: `now()`]
  last_login datetime [not null, default: `now()`]
  active boolean [not null, default: true]
}

Table partner_auth {
  id uuid [pk]
  partner_id uuid [not null, unique, ref: > partner.id]
  email varchar(255) [unique, not null]
  password_hash varchar(255) [not null]
  created_at datetime [not null, default: `now()`]
  last_login datetime [not null, default: `now()`]
  active boolean [not null, default: true]
}
