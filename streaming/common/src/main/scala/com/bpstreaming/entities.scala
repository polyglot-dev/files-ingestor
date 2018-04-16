package com.bpstreaming

final case class TCheck(
                         DeregisterCriticalServiceAfter: String,
                         Interval: String,
                         HTTP: String
                       )

final case class Consul(
                         Name: String,
                         Tags: List[String],
                         Address: String,
                         Port: Int,
                         EnableTagOverride: Boolean,
                         Check: TCheck
                       )