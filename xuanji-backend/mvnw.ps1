#!/usr/bin/env pwsh
# -----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# -----------------------------------------------------------------------------

# -----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, version 3.2.0
#
# Optional ENV vars:
#   MVNW_REPOURL - repo url base for downloading maven distribution
#   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven
#   MVNW_VERBOSE - true: enable verbose log; debug: trace the mvnw script; others: silence the output
# -----------------------------------------------------------------------------

$ErrorActionPreference = "Stop"
if ($env:MVNW_VERBOSE -eq "true") {
  $VerbosePreference = "Continue"
}

# OS specific support.  $var _must_ be set to either true or false.
$cygwin = $false
$msys = $false
$mingw = $false
$win = $false
switch -wildcard ( $env:OS ) {
  "Windows*" { $win = $true }
  "CYGWIN*"  { $cygwin = $true }
  "MINGW*"   { $mingw = $true }
  "MSYS*"    { $msys = $true }
}

# normalize distributionUrl
$scriptDir = $PSScriptRoot
$distributionUrl = (Get-Content -Raw "$scriptDir/.mvn/wrapper/maven-wrapper.properties" | ConvertFrom-StringData).distributionUrl
if (!$distributionUrl) {
  Write-Error "cannot read distributionUrl property in $scriptDir/.mvn/wrapper/maven-wrapper.properties"
}

switch -wildcard -casesensitive ( $($distributionUrl -replace '^.*/','') ) {
  "maven-mvnd-*" {
    $USE_MVND = $true
    $distributionUrl = $distributionUrl -replace '-bin\.[^.]*$',"-windows-amd64.zip"
    $MVN_CMD = "mvnd.cmd"
    break
  }
  default {
    $USE_MVND = $false
    $MVN_CMD = $script -replace '^mvnw','mvn'
    break
  }
}

# apply MVNW_REPOURL and calculate MAVEN_HOME
# maven home pattern: ~/.m2/wrapper/dists/{apache-maven-<version>,maven-mvnd-<version>-<platform>}/<hash>
if ($env:MVNW_REPOURL) {
  $MVNW_REPO_PATTERN = if ($USE_MVND) { "/org/apache/maven/" } else { "/maven/mvnd/" }
  $distributionUrl = "$env:MVNW_REPOURL$MVNW_REPO_PATTERN$($distributionUrl -replace '^.*'+$MVNW_REPO_PATTERN,'')"
}

$distributionUrlName = $distributionUrl -replace '^.*/',''
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]*$','' -replace '-bin$',''
$MAVEN_HOME_PARENT = "$HOME/.m2/wrapper/dists/$distributionUrlNameMain"
if ($env:MAVEN_USER_HOME) {
  $MAVEN_HOME_PARENT = "$env:MAVEN_USER_HOME/wrapper/dists/$distributionUrlNameMain"
}
$MAVEN_HOME_NAME = ([System.Security.Cryptography.MD5]::Create().ComputeHash([System.Text.Encoding]::UTF8.GetBytes($distributionUrl)) | ForEach-Object {$_.ToString("x2")}) -join ''
$MAVEN_HOME = "$MAVEN_HOME_PARENT/$MAVEN_HOME_NAME"

if (Test-Path -Path "$MAVEN_HOME" -PathType Container) {
  Write-Verbose "found existing MAVEN_HOME at $MAVEN_HOME"
  Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
  exit $?
}

if (! $distributionUrlNameMain -or ($distributionUrlName -eq $distributionUrlNameMain)) {
  Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
}

# prepare tmp dir
$TMP_DOWNLOAD_DIR = "$env:TEMP/mvnw-$([System.Guid]::NewGuid())"
New-Item -ItemType Directory -Path "$TMP_DOWNLOAD_DIR" | Out-Null

try {
  Write-Verbose "Downloading from: $distributionUrl"
  Write-Verbose "Downloading to: $TMP_DOWNLOAD_DIR/$distributionUrlName"

  $webclient = New-Object System.Net.WebClient
  if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
    $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
  }
  [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
  $webclient.DownloadFile($distributionUrl, "$TMP_DOWNLOAD_DIR/$distributionUrlName") | Out-Null

  Write-Verbose "Unzipping $TMP_DOWNLOAD_DIR/$distributionUrlName to $MAVEN_HOME_PARENT"
  Expand-Archive -Path "$TMP_DOWNLOAD_DIR/$distributionUrlName" -DestinationPath "$MAVEN_HOME_PARENT" -Force | Out-Null
} catch {
  Write-Error "cannot download $distributionUrl to $TMP_DOWNLOAD_DIR/$distributionUrlName"
} finally {
  try { Remove-Item -Path "$TMP_DOWNLOAD_DIR" -Recurse -Force | Out-Null } catch {
    Write-Warning "cannot remove $TMP_DOWNLOAD_DIR"
  }
}

$distributionUrlNameMainWithoutBin = $distributionUrlNameMain -replace '-bin$',''
$MAVEN_HOME = "$MAVEN_HOME_PARENT/$distributionUrlNameMainWithoutBin"

Write-Verbose "MAVEN_HOME=$MAVEN_HOME"
Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
