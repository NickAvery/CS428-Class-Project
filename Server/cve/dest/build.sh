#!/bin/sh

#
# This shell script attempts to automatically assign the correct platform name
# for UNIX-based configurations.
#
SYS=`uname -a | sed 's/ /_/g'`
case "$SYS" in
   Darwin*Version_10*i386)
      make X-Configure name=amd64_macos;;
   Darwin*Version_9*i386)
      make X-Configure name=intel_macos;;
   Darwin*Power_Macintosh)
      make X-Configure name=ppc_macos;;
   Darwin*powerpc)
      make X-Configure name=ppc_macos;;
   FreeBSD*i386)
      make X-Configure name=intel_freebsd;;
   Linux*alpha*)
      make X-Configure name=alpha_linux;;
   Linux*i686*)
      make X-Configure name=intel_linux;;
   Linux*sparc64*)
      make X-Configure name=sun_linux;;
   Linux*x86_64*)
      make X-Configure name=amd64_linux;;
   SunOS*sparc*)
      make X-Configure name=sparc_solaris;;
   SunOS*i86pc)
      make X-Configure name=amd64_solaris;;
   *)
      echo 1>&2 "don't know how to select configuration for $SYS, use 'make [X-]Configure name=system'"
      exit 1;;
esac

