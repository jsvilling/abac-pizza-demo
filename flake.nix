{
  inputs = {
    utils.url = "github:numtide/flake-utils";
  };
  outputs = { self, nixpkgs, utils }: utils.lib.eachDefaultSystem (system:
    let
      pkgs = nixpkgs.legacyPackages.${system};
      jdk = pkgs.jdk25;
    in
    {
      devShell = pkgs.mkShell {
        buildInputs = with pkgs; [
          jdk
          quarkus
          maven
        ];

        shellHook = ''
           rm -rf .jdk
           ln -s ${jdk} .jdk
        '';
      };
    }
  );
}
