#!/usr/bin/perl

@filelist=`find . -name "*.fnt"`;

foreach (@filelist) {
  $tmp = $_;
  chomp ($tmp);
  $tmp =~ s/\.\///g;
  $tmp =~ s/\.fnt//;
  printf "fontdata[" . $tmp . "] = new byte[] {";
  system('hexdump -v -e \'1 1 "0x%02x, "\' ' . $_);
  printf("};\n");
}
